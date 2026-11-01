package dev.viper.zbkits.hooks;

import dev.viper.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HooksImpl {
   public static void of(JavaPlugin plugin) {
      PluginManager pm = plugin.getServer().getPluginManager();
      ConsoleCommandSender log = Bukkit.getServer().getConsoleSender();

      for(SupportedPlugins supportedPlugin : SupportedPlugins.values()) {
         if (isPluginEnabled(pm, supportedPlugin)) {
            log.sendMessage(ColorizeText.hex("&c&lzbKits &7› &fFound &#ff2e2e" + supportedPlugin.getName() + "&f, Hooking into it"));
            supportedPlugin.hook();
            log.sendMessage(ColorizeText.hex("&c&lzbKits &7› &fHooked into &#ff2e2e" + supportedPlugin.getName() + "&f!"));
         }
      }

   }

   private static boolean isPluginEnabled(PluginManager pluginManager, SupportedPlugins plugin) {
      return pluginManager.isPluginEnabled(plugin.getName());
   }
}
