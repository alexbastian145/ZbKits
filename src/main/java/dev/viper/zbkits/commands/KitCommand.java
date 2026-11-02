package dev.viper.zbkits.commands;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.hooks.worldguard.WorldGuardHook;
import dev.viper.zbkits.menus.KitsMenu;
import dev.viper.zbkits.utils.Messages;
import dev.viper.zbkits.utils.wg.BlacklistedRegion;
import dev.viper.utils.command.ZarbunexCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitCommand extends ZarbunexCommand<zbKits> {
   public KitCommand(zbKits plugin) {
      super(plugin, "zbkits", "kit");
      this.setAliases(new String[]{"k"});
      this.setUsage("");
      this.registerCommand();
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
      if (sender instanceof Player player) {
         if (WorldGuardHook.get().isEnabled() && BlacklistedRegion.isInBlacklistedRegion(player)) {
            Messages.send(player, "blacklisted_region");
            return true;
         } else {
            KitsMenu.openKitMenu(player).open(player);
            return true;
         }
      } else {
         return false;
      }
   }
}
