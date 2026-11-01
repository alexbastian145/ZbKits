package dev.viper.zbkits.utils.config;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MenuConfig {
   private final FileConfiguration config;

   public MenuConfig(Plugin plugin, String fileName) {
      File file = new File(plugin.getDataFolder(), fileName);
      if (!file.exists()) {
         plugin.saveResource(fileName, false);
      }

      this.config = YamlConfiguration.loadConfiguration(file);
   }

   public static void of(Plugin plugin) {
      new MenuConfig(plugin, "menus/kits_menu.yml");
      new MenuConfig(plugin, "menus/kiteditor_menu.yml");
      new MenuConfig(plugin, "menus/enderchest_editor_menu.yml");
      new MenuConfig(plugin, "menus/autorekit_menu.yml");
      new MenuConfig(plugin, "menus/premadekit_menu.yml");
      new MenuConfig(plugin, "menus/kitroom_menu.yml");
   }

   public FileConfiguration getConfig() {
      return this.config;
   }
}
