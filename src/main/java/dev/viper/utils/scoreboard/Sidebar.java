package dev.viper.utils.scoreboard;

import dev.viper.utils.reflection.ReflectionUtils;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class Sidebar extends SidebarHandler<Component> {
   private static final MethodHandle COMPONENT_METHOD;
   private static final Object EMPTY_COMPONENT;
   private static final boolean ADVENTURE_SUPPORT = ReflectionUtils.optionalClass("io.papermc.paper.adventure.PaperAdventure").isPresent();

   public Sidebar(Player player) {
      super(player);
   }

   public static Sidebar sidebar(Player player) {
      return new Sidebar(player);
   }

   protected void sendLineChange(int score) throws Throwable {
      Component line = (Component)this.lineByScore(score);
      this.sendTeamPacket(score, SidebarHandler.TeamMode.UPDATE, line, null);
   }

   protected Object toMinecraftComponent(Component component) throws Throwable {
      if (component == null) {
         return EMPTY_COMPONENT;
      } else if (!ADVENTURE_SUPPORT) {
         String legacy = this.serializeLine(component);
         return Array.get(COMPONENT_METHOD.invoke(legacy), 0);
      } else {
         return COMPONENT_METHOD.invoke(component);
      }
   }

   protected String serializeLine(Component value) {
      return LegacyComponentSerializer.legacySection().serialize(value);
   }

   protected Component emptyLine() {
      return Component.empty();
   }

   static {
      MethodHandles.Lookup lookup = MethodHandles.lookup();

      try {
         if (ADVENTURE_SUPPORT) {
            Class<?> paperAdventure = Class.forName("io.papermc.paper.adventure.PaperAdventure");
            Method method = paperAdventure.getDeclaredMethod("asVanilla", Component.class);
            COMPONENT_METHOD = lookup.unreflect(method);
            EMPTY_COMPONENT = COMPONENT_METHOD.invoke(Component.empty());
         } else {
            Class<?> craftChatMessageClass = ReflectionUtils.obcClass("util.CraftChatMessage");
            COMPONENT_METHOD = lookup.unreflect(craftChatMessageClass.getMethod("fromString", String.class));
            EMPTY_COMPONENT = Array.get(COMPONENT_METHOD.invoke(""), 0);
         }

      } catch (Throwable t) {
         throw new ExceptionInInitializerError(t);
      }
   }
}
