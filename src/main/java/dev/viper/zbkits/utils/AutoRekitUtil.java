package dev.viper.zbkits.utils;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.api.AutoRekitAPI;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.utils.text.color.ColorizeText;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRekitUtil implements AutoRekitAPI {
   private static ConfigManager configManager;
   private final MenuConfig CONFIG = new MenuConfig(zbKits.getInstance(), "menus/autorekit_menu.yml");

   public AutoRekitUtil(ConfigManager configManager) {
      AutoRekitUtil.configManager = configManager;
   }

   public static void of(JavaPlugin plugin) {
      configManager = ConfigManager.get(plugin);
      configManager.create("data/autorekit.yml");
   }

   public void set(Player player, Boolean enabled, String kitName) {
      String playerName = player.getUniqueId().toString();
      configManager.set("data/autorekit.yml", playerName + ".auto_rekit.enabled", enabled);
      configManager.set("data/autorekit.yml", playerName + ".auto_rekit.kit", kitName);
      this.save();
   }

   public void setKit(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      configManager.set("data/autorekit.yml", playerName + ".auto_rekit.kit", kitName);
      this.save();
   }

   public void toggle(Player player) {
      String playerName = player.getUniqueId().toString();
      boolean currentSetting = configManager.getConfig("data/autorekit.yml").getBoolean(playerName + ".auto_rekit.enabled");
      configManager.set("data/autorekit.yml", playerName + ".auto_rekit.enabled", !currentSetting);
      this.save();
   }

   public boolean get(Player player) {
      String playerName = player.getUniqueId().toString();
      return configManager.getConfig("data/autorekit.yml").getBoolean(playerName + ".auto_rekit.enabled", false);
   }

   public String getKit(Player player) {
      String playerName = player.getUniqueId().toString();
      return configManager.getConfig("data/autorekit.yml").getString(playerName + ".auto_rekit.kit", "Kit 1");
   }

   public boolean isEnabled(Player player) {
      String playerName = player.getUniqueId().toString();
      return configManager.getConfig("data/autorekit.yml").getBoolean(playerName + ".auto_rekit.enabled", false);
   }

   public boolean hasAutoRekit(Player player) {
      String playerName = player.getUniqueId().toString();
      return configManager.contains("data/autorekit.yml", playerName + ".auto_rekit");
   }

   public String status(Player player) {
      boolean autoRekitEnabled = this.get(player);
      String enabled = this.CONFIG.getConfig().getString("auto_rekit.placeholders.enabled", "&#7cff6eEnabled");
      String disabled = this.CONFIG.getConfig().getString("auto_rekit.placeholders.disabled", "&#ffa6a6Disabled");
      return autoRekitEnabled ? ColorizeText.hex(enabled) : ColorizeText.hex(disabled);
   }

   private void save() {
      configManager.saveConfig("data/autorekit.yml");
   }
}
