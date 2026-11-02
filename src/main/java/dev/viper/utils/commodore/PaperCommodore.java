package dev.viper.utils.commodore;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

final class PaperCommodore extends AbstractCommodore implements Commodore, Listener {
   private final List<CommodoreCommand> commands = new ArrayList();

   PaperCommodore(Plugin plugin) {
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }

   public void register(LiteralCommandNode<?> node) {
      Objects.requireNonNull(node, "node");
      this.commands.add(new CommodoreCommand(node, (Predicate)null));
   }

   public void register(Command command, LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
      Objects.requireNonNull(command, "command");
      Objects.requireNonNull(node, "node");
      Objects.requireNonNull(permissionTest, "permissionTest");

      try {
         setRequiredHackyFieldsRecursively(node, DUMMY_SUGGESTION_PROVIDER);
      } catch (Throwable e) {
         e.printStackTrace();
      }

      Collection<String> aliases = getAliases(command);
      if (!aliases.contains(node.getLiteral())) {
         node = renameLiteralNode(node, command.getName());
      }

      for(String alias : aliases) {
         if (node.getLiteral().equals(alias)) {
            this.commands.add(new CommodoreCommand(node, permissionTest));
         } else {
            LiteralCommandNode<?> redirectNode = LiteralArgumentBuilder.<Object>literal(alias).redirect((com.mojang.brigadier.tree.CommandNode<Object>)(com.mojang.brigadier.tree.CommandNode<?>)node).build();
            this.commands.add(new CommodoreCommand(redirectNode, permissionTest));
         }
      }

   }

   @EventHandler
   public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
      if (event.isAsynchronous() || !event.hasFiredAsync()) {
         for(CommodoreCommand command : this.commands) {
            command.apply(event.getPlayer(), event.getCommandNode());
         }
      }

   }

   static void ensureSetup() {
   }

   static {
      try {
         Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent");
      } catch (ClassNotFoundException e) {
         throw new UnsupportedOperationException("Not running on modern Paper!", e);
      }
   }

   private static final class CommodoreCommand {
      private final LiteralCommandNode<?> node;
      private final Predicate<? super Player> permissionTest;

      private CommodoreCommand(LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
         this.node = node;
         this.permissionTest = permissionTest;
      }

      public void apply(Player player, RootCommandNode<?> root) {
         if (this.permissionTest == null || this.permissionTest.test(player)) {
            AbstractCommodore.removeChild(root, this.node.getName());
            ((RootCommandNode<Object>)root).addChild((com.mojang.brigadier.tree.CommandNode<Object>)(com.mojang.brigadier.tree.CommandNode<?>)this.node);
         }
      }
   }
}
