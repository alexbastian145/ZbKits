package dev.viper.zbkits.commands.admin;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.menus.admin.KitViewMenu;
import dev.viper.utils.command.ZarbunexCommand;
import dev.viper.utils.text.color.ColorizeText;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitsAdminCommand extends ZarbunexCommand<zbKits> {
   public KitsAdminCommand(zbKits plugin) {
      super(plugin, "zbkits", "kitsadmin");
      this.addTabbComplete(0, new String[]{"view"});
      this.addTabbComplete(0, new String[]{"clear"});
      this.setUsage("");
      this.setPermission("zbkits.admin");
      this.registerCommand();
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (!(sender instanceof Player executor)) {
         return true;
      } else if (args.length >= 2 && (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("clear"))) {
         OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
         if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
            executor.sendMessage(ColorizeText.hex("&#ffa6a6Player not found."));
            return true;
         } else {
            switch (args[0].toLowerCase()) {
               case "view":
                  if (args.length != 2) {
                     sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitsadmin view <player>"));
                     return true;
                  }

                  KitViewMenu.openKitSelectMenu(executor, targetPlayer.getName());
                  break;
               case "clear":
                  if (args.length < 3) {
                     sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitsadmin clear <player> <kit name>"));
                     return true;
                  }

                  String kitName = (String)Stream.of(args).skip(2L).collect(Collectors.joining(" "));
                  zbKits.getKitUtil().delete((Player)targetPlayer, kitName);
                  zbKits.getEnderChestUtil().delete((Player)targetPlayer, kitName);
                  executor.sendMessage(ColorizeText.hex("&#7cff6e" + kitName + " has been cleared for player " + targetPlayer.getName() + "."));
            }

            return true;
         }
      } else {
         sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitsadmin <view/clear> <player> [kit name]"));
         return true;
      }
   }

   public @NotNull List<String> tabComplete(CommandSender sender, String alias, String[] args) {
      if (args.length != 2 || !args[0].equalsIgnoreCase("view") && !args[0].equalsIgnoreCase("clear")) {
         if (args.length == 3 && args[0].equalsIgnoreCase("clear")) {
            int numKits = zbKits.getInstance().getConfig().getInt("kits");
            return (List)IntStream.rangeClosed(1, numKits).mapToObj((i) -> "Kit " + i).collect(Collectors.toList());
         } else {
            return super.tabComplete(sender, alias, args);
         }
      } else {
         return (List)Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
      }
   }
}
