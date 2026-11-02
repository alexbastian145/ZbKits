package dev.viper.utils.scoreboard;

import dev.viper.utils.reflection.ReflectionUtils;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class SidebarHandler<S> {
   private static final Map<Class<?>, Field[]> PACKETS = new HashMap(8);
   protected static final String[] COLOR_CODES = (String[])Arrays.stream(ChatColor.values()).map(Object::toString).toArray((x$0) -> new String[x$0]);
   private static final VersionType VERSION_TYPE;
   private static final Class<?> CHAT_COMPONENT_CLASS;
   private static final Class<?> CHAT_FORMAT_ENUM;
   private static final Object RESET_FORMATTING;
   private static final MethodHandle PLAYER_CONNECTION;
   private static final MethodHandle SEND_PACKET;
   private static final MethodHandle PLAYER_GET_HANDLE;
   private static final ReflectionUtils.PacketConstructor PACKET_SB_OBJ;
   private static final ReflectionUtils.PacketConstructor PACKET_SB_DISPLAY_OBJ;
   private static final ReflectionUtils.PacketConstructor PACKET_SB_SCORE;
   private static final ReflectionUtils.PacketConstructor PACKET_SB_TEAM;
   private static final ReflectionUtils.PacketConstructor PACKET_SB_SERIALIZABLE_TEAM;
   private static final Class<?> DISPLAY_SLOT_TYPE;
   private static final Class<?> ENUM_SB_HEALTH_DISPLAY;
   private static final Class<?> ENUM_SB_ACTION;
   private static final Object SIDEBAR_DISPLAY_SLOT;
   private static final Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
   private static final Object ENUM_SB_ACTION_CHANGE;
   private static final Object ENUM_SB_ACTION_REMOVE;
   private final Player player;
   private final String uniqueId;
   private final List<S> lines = new ArrayList();
   private S title = (S)this.emptyLine();
   private boolean deleted = false;

   protected SidebarHandler(Player player) {
      this.player = (Player)Objects.requireNonNull(player, "player");
      this.uniqueId = "sidebar-" + Integer.toHexString(ThreadLocalRandom.current().nextInt());

      try {
         this.sendObjectivePacket(SidebarHandler.ObjectiveMode.CREATE);
         this.sendDisplayObjectivePacket();
      } catch (Throwable t) {
         throw new RuntimeException("Unable to create scoreboard", t);
      }
   }

   public S title() {
      return this.title;
   }

   public void updateTitle(S title) {
      if (!this.title.equals(Objects.requireNonNull(title, "title"))) {
         this.title = title;

         try {
            this.sendObjectivePacket(SidebarHandler.ObjectiveMode.UPDATE);
         } catch (Throwable t) {
            throw new RuntimeException("Unable to update scoreboard title", t);
         }
      }
   }

   public List<S> lines() {
      return new ArrayList(this.lines);
   }

   public S line(int line) {
      this.checkLineNumber(line, true, false);
      return (S)this.lines.get(line);
   }

   public synchronized void updateLine(int line, S text) {
      this.checkLineNumber(line, false, true);

      try {
         if (line < this.size()) {
            this.lines.set(line, text);
            this.sendLineChange(this.scoreByLine(line));
         } else {
            List<S> newLines = new ArrayList(this.lines);
            if (line > this.size()) {
               for(int i = this.size(); i < line; ++i) {
                  newLines.add(this.emptyLine());
               }
            }

            newLines.add(text);
            this.updateLines(newLines);
         }
      } catch (Throwable t) {
         throw new RuntimeException("Unable to update scoreboard lines", t);
      }
   }

   public synchronized void removeLine(int line) {
      this.checkLineNumber(line, false, false);
      if (line < this.size()) {
         List<S> newLines = new ArrayList(this.lines);
         newLines.remove(line);
         this.updateLines(newLines);
      }
   }

   @SafeVarargs
   public final void updateLines(S... lines) {
      this.updateLines(Arrays.asList(lines));
   }

   public synchronized void updateLines(Collection<S> lines) {
      Objects.requireNonNull(lines, "lines");
      this.checkLineNumber(lines.size(), false, true);
      List<S> oldLines = new ArrayList(this.lines);
      this.lines.clear();
      this.lines.addAll(lines);
      int linesSize = this.lines.size();

      try {
         if (oldLines.size() != linesSize) {
            List<S> oldLinesCopy = new ArrayList(oldLines);
            if (oldLines.size() > linesSize) {
               for(int i = oldLinesCopy.size(); i > linesSize; --i) {
                  this.sendTeamPacket(i - 1, SidebarHandler.TeamMode.REMOVE);
                  this.sendScorePacket(i - 1, SidebarHandler.ScoreboardAction.REMOVE);
                  oldLines.remove(0);
               }
            } else {
               for(int i = oldLinesCopy.size(); i < linesSize; ++i) {
                  this.sendScorePacket(i, SidebarHandler.ScoreboardAction.CHANGE);
                  this.sendTeamPacket(i, SidebarHandler.TeamMode.CREATE, null, null);
               }
            }
         }

         for(int i = 0; i < linesSize; ++i) {
            if (!Objects.equals(this.lineByScore(oldLines, i), this.lineByScore(i))) {
               this.sendLineChange(i);
            }
         }

      } catch (Throwable t) {
         throw new RuntimeException("Unable to update scoreboard lines", t);
      }
   }

   public Player player() {
      return this.player;
   }

   public String uniqueId() {
      return this.uniqueId;
   }

   public boolean isDeleted() {
      return this.deleted;
   }

   public int size() {
      return this.lines.size();
   }

   public void delete() {
      try {
         for(int i = 0; i < this.lines.size(); ++i) {
            this.sendTeamPacket(i, SidebarHandler.TeamMode.REMOVE);
         }

         this.sendObjectivePacket(SidebarHandler.ObjectiveMode.REMOVE);
      } catch (Throwable t) {
         throw new RuntimeException("Unable to delete scoreboard", t);
      }

      this.deleted = true;
   }

   protected abstract void sendLineChange(int var1) throws Throwable;

   protected abstract Object toMinecraftComponent(S var1) throws Throwable;

   protected abstract String serializeLine(S var1);

   protected abstract S emptyLine();

   private void checkLineNumber(int line, boolean checkInRange, boolean checkMax) {
      if (line < 0) {
         throw new IllegalArgumentException("Line number must be positive");
      } else if (checkInRange && line >= this.lines.size()) {
         throw new IllegalArgumentException("Line number must be under " + this.lines.size());
      } else if (checkMax && line >= COLOR_CODES.length - 1) {
         throw new IllegalArgumentException("Line number is too high: " + line);
      }
   }

   protected int scoreByLine(int line) {
      return this.lines.size() - line - 1;
   }

   protected S lineByScore(int score) {
      return (S)this.lineByScore(this.lines, score);
   }

   protected S lineByScore(List<S> lines, int score) {
      return (S)(score < lines.size() ? lines.get(lines.size() - score - 1) : null);
   }

   protected void sendObjectivePacket(ObjectiveMode mode) throws Throwable {
      Object packet = PACKET_SB_OBJ.invoke();
      this.field(packet, String.class, this.uniqueId);
      this.field(packet, Integer.TYPE, mode.ordinal());
      if (mode != SidebarHandler.ObjectiveMode.REMOVE) {
         this.componentField(packet, this.title, 1);
         if (SidebarHandler.VersionType.V1_8.isHigherOrEqual()) {
            this.field(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
         }
      } else if (VERSION_TYPE == SidebarHandler.VersionType.V1_7) {
         this.field(packet, String.class, "", 1);
      }

      this.sendPacket(packet);
   }

   protected void sendDisplayObjectivePacket() throws Throwable {
      Object packet = PACKET_SB_DISPLAY_OBJ.invoke();
      this.field(packet, DISPLAY_SLOT_TYPE, SIDEBAR_DISPLAY_SLOT);
      this.field(packet, String.class, this.uniqueId);
      this.sendPacket(packet);
   }

   protected void sendScorePacket(int score, ScoreboardAction action) throws Throwable {
      Object packet = PACKET_SB_SCORE.invoke();
      this.field(packet, String.class, COLOR_CODES[score], 0);
      if (SidebarHandler.VersionType.V1_8.isHigherOrEqual()) {
         Object enumAction = action == SidebarHandler.ScoreboardAction.REMOVE ? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE;
         this.field(packet, ENUM_SB_ACTION, enumAction);
      } else {
         this.field(packet, Integer.TYPE, action.ordinal(), 1);
      }

      if (action == SidebarHandler.ScoreboardAction.CHANGE) {
         this.field(packet, String.class, this.uniqueId, 1);
         this.field(packet, Integer.TYPE, score);
      }

      this.sendPacket(packet);
   }

   protected void sendTeamPacket(int score, TeamMode mode) throws Throwable {
      this.sendTeamPacket(score, mode, null, null);
   }

   protected void sendTeamPacket(int score, TeamMode mode, S prefix, S suffix) throws Throwable {
      if (mode != SidebarHandler.TeamMode.ADD_PLAYERS && mode != SidebarHandler.TeamMode.REMOVE_PLAYERS) {
         Object packet = PACKET_SB_TEAM.invoke();
         String var10003 = this.uniqueId;
         this.field(packet, String.class, var10003 + ":" + score);
         this.field(packet, Integer.TYPE, mode.ordinal(), VERSION_TYPE == SidebarHandler.VersionType.V1_8 ? 1 : 0);
         if (mode == SidebarHandler.TeamMode.REMOVE) {
            this.sendPacket(packet);
         } else {
            if (SidebarHandler.VersionType.V1_17.isHigherOrEqual()) {
               Object team = PACKET_SB_SERIALIZABLE_TEAM.invoke();
               this.componentField(team, (S)null, 0);
               this.field(team, CHAT_FORMAT_ENUM, RESET_FORMATTING);
               this.componentField(team, prefix, 1);
               this.componentField(team, suffix, 2);
               this.field(team, String.class, "always", 0);
               this.field(team, String.class, "always", 1);
               this.field(packet, Optional.class, Optional.of(team));
            } else {
               this.componentField(packet, prefix, 2);
               this.componentField(packet, suffix, 3);
               this.field(packet, String.class, "always", 4);
               this.field(packet, String.class, "always", 5);
            }

            if (mode == SidebarHandler.TeamMode.CREATE) {
               this.field(packet, Collection.class, Collections.singletonList(COLOR_CODES[score]));
            }

            this.sendPacket(packet);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   private void sendPacket(Object packet) throws Throwable {
      if (this.deleted) {
         throw new IllegalStateException("This FastBoard is deleted");
      } else {
         if (this.player.isOnline()) {
            Object entityPlayer = PLAYER_GET_HANDLE.invoke(this.player);
            Object playerConnection = PLAYER_CONNECTION.invoke(entityPlayer);
            SEND_PACKET.invoke(playerConnection, packet);
         }

      }
   }

   private void field(Object object, Class<?> fieldType, Object value) throws ReflectiveOperationException {
      this.field(object, fieldType, value, 0);
   }

   private void field(Object packet, Class<?> fieldType, Object value, int count) throws ReflectiveOperationException {
      int i = 0;

      for(Field field : (Field[])PACKETS.get(packet.getClass())) {
         if (field.getType() == fieldType && count == i++) {
            field.set(packet, value);
         }
      }

   }

   private void componentField(Object packet, S value, int count) throws Throwable {
      if (!SidebarHandler.VersionType.V1_13.isHigherOrEqual()) {
         String line = value != null ? this.serializeLine(value) : "";
         this.field(packet, String.class, line, count);
      } else {
         int i = 0;

         for(Field field : (Field[])PACKETS.get(packet.getClass())) {
            if ((field.getType() == String.class || field.getType() == CHAT_COMPONENT_CLASS) && count == i++) {
               field.set(packet, this.toMinecraftComponent(value));
            }
         }

      }
   }

   static {
      try {
         MethodHandles.Lookup lookup = MethodHandles.lookup();
         if (ReflectionUtils.isRepackaged()) {
            VERSION_TYPE = SidebarHandler.VersionType.V1_17;
         } else if (ReflectionUtils.nmsOptionalClass((String)null, "ScoreboardServer$Action").isPresent()) {
            VERSION_TYPE = SidebarHandler.VersionType.V1_13;
         } else if (ReflectionUtils.nmsOptionalClass((String)null, "IScoreboardCriteria$EnumScoreboardHealthDisplay").isPresent()) {
            VERSION_TYPE = SidebarHandler.VersionType.V1_8;
         } else {
            VERSION_TYPE = SidebarHandler.VersionType.V1_7;
         }

         String gameProtocolPackage = "network.protocol.game";
         Class<?> craftPlayerClass = ReflectionUtils.obcClass("entity.CraftPlayer");
         Class<?> entityPlayerClass = ReflectionUtils.nmsClass("server.level", "EntityPlayer");
         Class<?> playerConnectionClass = ReflectionUtils.nmsClass("server.network", "PlayerConnection");
         Class<?> packetClass = ReflectionUtils.nmsClass("network.protocol", "Packet");
         Class<?> packetSbObjClass = ReflectionUtils.nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardObjective");
         Class<?> packetSbDisplayObjClass = ReflectionUtils.nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardDisplayObjective");
         Class<?> packetSbScoreClass = ReflectionUtils.nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardScore");
         Class<?> packetSbTeamClass = ReflectionUtils.nmsClass(gameProtocolPackage, "PacketPlayOutScoreboardTeam");
         Class<?> sbTeamClass = SidebarHandler.VersionType.V1_17.isHigherOrEqual() ? ReflectionUtils.innerClass(packetSbTeamClass, (innerClass) -> !innerClass.isEnum()) : null;
         Field playerConnectionField = (Field)Arrays.stream(entityPlayerClass.getFields()).filter((fieldx) -> fieldx.getType().isAssignableFrom(playerConnectionClass)).findFirst().orElseThrow(NoSuchFieldException::new);
         Method sendPacketMethod = (Method)Stream.concat(Arrays.stream(playerConnectionClass.getSuperclass().getMethods()), Arrays.stream(playerConnectionClass.getMethods())).filter((m) -> m.getParameterCount() == 1 && m.getParameterTypes()[0] == packetClass).findFirst().orElseThrow(NoSuchMethodException::new);
         Optional<Class<?>> displaySlotEnum = ReflectionUtils.nmsOptionalClass("world.scores", "DisplaySlot");
         CHAT_COMPONENT_CLASS = ReflectionUtils.nmsClass("network.chat", "IChatBaseComponent");
         CHAT_FORMAT_ENUM = ReflectionUtils.nmsClass((String)null, "EnumChatFormat");
         DISPLAY_SLOT_TYPE = (Class)displaySlotEnum.orElse(Integer.TYPE);
         RESET_FORMATTING = ReflectionUtils.enumValueOf(CHAT_FORMAT_ENUM, "RESET", 21);
         SIDEBAR_DISPLAY_SLOT = displaySlotEnum.isPresent() ? ReflectionUtils.enumValueOf(DISPLAY_SLOT_TYPE, "SIDEBAR", 1) : 1;
         PLAYER_GET_HANDLE = lookup.findVirtual(craftPlayerClass, "getHandle", MethodType.methodType(entityPlayerClass));
         PLAYER_CONNECTION = lookup.unreflectGetter(playerConnectionField);
         SEND_PACKET = lookup.unreflect(sendPacketMethod);
         PACKET_SB_OBJ = ReflectionUtils.findPacketConstructor(packetSbObjClass, lookup);
         PACKET_SB_DISPLAY_OBJ = ReflectionUtils.findPacketConstructor(packetSbDisplayObjClass, lookup);
         PACKET_SB_SCORE = ReflectionUtils.findPacketConstructor(packetSbScoreClass, lookup);
         PACKET_SB_TEAM = ReflectionUtils.findPacketConstructor(packetSbTeamClass, lookup);
         PACKET_SB_SERIALIZABLE_TEAM = sbTeamClass == null ? null : ReflectionUtils.findPacketConstructor(sbTeamClass, lookup);

         for(Class<?> clazz : Arrays.asList(packetSbObjClass, packetSbDisplayObjClass, packetSbScoreClass, packetSbTeamClass, sbTeamClass)) {
            if (clazz != null) {
               Field[] fields = (Field[])Arrays.stream(clazz.getDeclaredFields()).filter((fieldx) -> !Modifier.isStatic(fieldx.getModifiers())).toArray((x$0) -> new Field[x$0]);

               for(Field field : fields) {
                  field.setAccessible(true);
               }

               PACKETS.put(clazz, fields);
            }
         }

         if (SidebarHandler.VersionType.V1_8.isHigherOrEqual()) {
            String enumSbActionClass = SidebarHandler.VersionType.V1_13.isHigherOrEqual() ? "ScoreboardServer$Action" : "PacketPlayOutScoreboardScore$EnumScoreboardAction";
            ENUM_SB_HEALTH_DISPLAY = ReflectionUtils.nmsClass("world.scores.criteria", "IScoreboardCriteria$EnumScoreboardHealthDisplay");
            ENUM_SB_ACTION = ReflectionUtils.nmsClass("server", enumSbActionClass);
            ENUM_SB_HEALTH_DISPLAY_INTEGER = ReflectionUtils.enumValueOf(ENUM_SB_HEALTH_DISPLAY, "INTEGER", 0);
            ENUM_SB_ACTION_CHANGE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "CHANGE", 0);
            ENUM_SB_ACTION_REMOVE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "REMOVE", 1);
         } else {
            ENUM_SB_HEALTH_DISPLAY = null;
            ENUM_SB_ACTION = null;
            ENUM_SB_HEALTH_DISPLAY_INTEGER = null;
            ENUM_SB_ACTION_CHANGE = null;
            ENUM_SB_ACTION_REMOVE = null;
         }

      } catch (Throwable t) {
         throw new ExceptionInInitializerError(t);
      }
   }

   public static enum ObjectiveMode {
      CREATE,
      REMOVE,
      UPDATE;

   }

   public static enum TeamMode {
      CREATE,
      REMOVE,
      UPDATE,
      ADD_PLAYERS,
      REMOVE_PLAYERS;

   }

   public static enum ScoreboardAction {
      CHANGE,
      REMOVE;

   }

   static enum VersionType {
      V1_7,
      V1_8,
      V1_13,
      V1_17;

      public boolean isHigherOrEqual() {
         return SidebarHandler.VERSION_TYPE.ordinal() >= this.ordinal();
      }

   }
}
