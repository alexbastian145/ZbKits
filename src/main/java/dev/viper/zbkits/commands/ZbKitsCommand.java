package dev.viper.zbkits.commands;

import dev.viper.utils.command.ZarbunexCommand;
import dev.viper.zbkits.utils.Messages;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.zbkits.zbKits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ZbKitsCommand extends ZarbunexCommand<zbKits> {
   public ZbKitsCommand(zbKits plugin) {
      super(plugin, "zbkits", "zbkits");
      this.setAliases(new String[]{"zbk"});
      this.addTabbComplete(0, new String[]{"reload"});
      this.setPermission("zbkits.admin");
      this.setUsage("");
      this.registerCommand();
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
         zbKits plugin = this.getPlugin();
         int kitsBefore = plugin.getConfig().getInt("kits", 7);
         plugin.reloadConfig();
         MenuConfig.reloadAll();
         Messages.send(sender, "reload_success");
         if (plugin.getConfig().getInt("kits", 7) != kitsBefore) {
            Messages.send(sender, "reload_kits_note");
         }

         return true;
      } else {
         Messages.send(sender, "zbkits_usage");
         return true;
      }
   }
}
