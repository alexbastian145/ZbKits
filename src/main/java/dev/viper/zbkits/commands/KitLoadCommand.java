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

public class KitLoadCommand extends ZarbunexCommand<zbKits> {
   public KitLoadCommand(zbKits plugin, String name, int kit) {
      super(plugin, "zbkits", name);
      this.setAliases(new String[]{"k" + kit});
      this.setUsage("");
      this.registerCommand();
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (sender instanceof Player player) {
         if (WorldGuardHook.get().isEnabled() && BlacklistedRegion.isInBlacklistedRegion(player)) {
            String cannotUseHere = (String)Objects.requireNonNull(zbKits.getInstance().getConfig().getString("messages.blacklisted_region"));
            player.sendMessage(ColorizeText.hex(cannotUseHere));
            return true;
         } else {
            int kits = ((zbKits)this.getPlugin()).getConfig().getInt("kits", 7);
            String cmdName = command.getName();

            for(int i = 1; i <= kits; ++i) {
               if (cmdName.equalsIgnoreCase("kit" + i)) {
                  if (player.hasPermission("zbkits." + cmdName)) {
                     zbKits.getKitUtil().load(player, "Kit " + i);
                  } else {
                     player.sendMessage(ColorizeText.hex("&#ffa6a6You don't have permission to use Kit " + i + "."));
                  }

                  return true;
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }
}
