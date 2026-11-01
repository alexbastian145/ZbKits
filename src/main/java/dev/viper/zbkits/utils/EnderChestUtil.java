package dev.viper.zbkits.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.api.EnderChestAPI;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.text.color.ColorizeText;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderChestUtil implements EnderChestAPI {
   private static ConfigManager configManager;
   private final Logger logger = Logger.getLogger(EnderChestUtil.class.getName());

   public EnderChestUtil(ConfigManager configManager) {
      EnderChestUtil.configManager = configManager;
   }

   public static void of(JavaPlugin plugin) {
      configManager = ConfigManager.get(plugin);
      configManager.create("data/enderchest.yml");
   }

   public void save(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      if (this.exists(player, kitName)) {
         this.delete(player, kitName);
      }

      for(int i = 0; i < 27; ++i) {
         ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
         if (item != null) {
            configManager.set("data/enderchest.yml", playerName + "." + kitName + ".enderchest." + i, item);
         }
      }

      try {
         configManager.saveConfig("data/enderchest.yml");
         String enderchestSaved = ((String)Objects.requireNonNull(zbKits.getInstance().getConfig().getString("messages.enderchest_saved"))).replace("%kit%", kitName);
         player.sendMessage(ColorizeText.hex(enderchestSaved));
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save ender chest", e);
      }

   }

   public void load(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      player.getEnderChest().clear();
      ConfigurationSection section = configManager.getConfig("data/enderchest.yml").getConfigurationSection(playerName + "." + kitName + ".enderchest");
      if (section != null) {
         for(String key : section.getKeys(false)) {
            ItemStack item = section.getItemStack(key);
            if (item != null) {
               player.getEnderChest().setItem(Integer.parseInt(key), item);
            }
         }
      }

   }

   public void set(Player player, String kitName, GuiBuilder inventory) {
      String playerName = player.getUniqueId().toString();
      ConfigurationSection section = configManager.getConfig("data/enderchest.yml").getConfigurationSection(playerName + "." + kitName + ".enderchest");
      if (section != null) {
         for(String key : section.getKeys(false)) {
            ItemStack item = section.getItemStack(key);
            if (item != null) {
               inventory.setItem(Integer.parseInt(key), item);
            }
         }
      }

   }

   public void delete(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      configManager.set("data/enderchest.yml", playerName + "." + kitName + ".enderchest", (Object)null);

      try {
         configManager.saveConfig("data/enderchest.yml");
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save ender chest", e);
      }

   }

   public boolean exists(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      return configManager.contains("data/enderchest.yml", playerName + "." + kitName + ".enderchest");
   }

   @CanIgnoreReturnValue
   public void saveAll() {
      try {
         configManager.saveConfig("data/enderchest.yml");
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save ender chest", e);
      }

   }
}
