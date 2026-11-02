package dev.viper.zbkits.utils.config;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MenuConfig {
   private static final List<MenuConfig> INSTANCES = new CopyOnWriteArrayList<>();
   private final File file;
   private volatile FileConfiguration config;

   public MenuConfig(Plugin plugin, String fileName) {
      this.file = new File(plugin.getDataFolder(), fileName);
      if (!file.exists()) {
         plugin.saveResource(fileName, false);
      }

      this.config = YamlConfiguration.loadConfiguration(file);
      INSTANCES.add(this);
   }

   /** Re-reads every loaded menu file from disk. */
   public static void reloadAll() {
      for(MenuConfig menuConfig : INSTANCES) {
         menuConfig.config = YamlConfiguration.loadConfiguration(menuConfig.file);
      }

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
