package dev.viper.zbkits.commands;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.hooks.worldguard.WorldGuardHook;
import dev.viper.zbkits.utils.wg.BlacklistedRegion;
import dev.viper.utils.command.ZarbunexCommand;
import dev.viper.utils.text.color.ColorizeText;
import java.util.Objects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PremadeKitCommand extends ZarbunexCommand<zbKits> {
   public PremadeKitCommand(zbKits plugin) {
      super(plugin, "zbkits", "premadekit");
      this.addTabbComplete(0, "zbkits.admin", (String[])null, new String[]{"save"});
      this.setUsage("");
      this.registerCommand();
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (sender instanceof Player player) {
         if (WorldGuardHook.get().isEnabled() && BlacklistedRegion.isInBlacklistedRegion(player)) {
            String cannotUseHere = (String)Objects.requireNonNull(zbKits.getInstance().getConfig().getString("messages.blacklisted_region"));
            player.sendMessage(ColorizeText.hex(cannotUseHere));
            return true;
         } else if (args.length == 0) {
            zbKits.getPremadeKitUtil().load(player);
            return true;
         } else if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
            if (player.hasPermission("zbkits.admin")) {
               zbKits.getPremadeKitUtil().save(player);
            } else {
               String noPerm = (String)Objects.requireNonNull(zbKits.getInstance().getConfig().getString("messages.no_permission"));
               player.sendMessage(ColorizeText.hex(noPerm));
            }

            return true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
