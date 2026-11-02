package dev.viper.zbkits.utils;

import dev.viper.zbkits.api.KitRoomAPI;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class KitRoomUtil implements KitRoomAPI {
   private static ConfigManager configManager;
   private final Logger logger = Logger.getLogger(KitRoomUtil.class.getName());

   public KitRoomUtil(ConfigManager configManager) {
      KitRoomUtil.configManager = configManager;
   }

   public static void of(JavaPlugin plugin) {
      configManager = ConfigManager.get(plugin);
      configManager.create("data/kitroom.yml");
   }

   public void save(Player player, String category) {
      if (this.exists(category)) {
         this.delete(category);
      }

      for(int i = 0; i < 45; ++i) {
         ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
         if (item != null) {
            configManager.set("data/kitroom.yml", "categories." + category + "." + i, item);
         }
      }

      try {
         configManager.saveConfig("data/kitroom.yml");
         Messages.send(player, "kitroom_category_saved", "%category%", category);
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save category", e);
      }

   }

   public void load(GuiBuilder inventory, String category) {
      for(int i = 0; i < 45; ++i) {
         inventory.setItem(i, new ItemStack(Material.AIR));
         ItemStack item = configManager.getConfig("data/kitroom.yml").getItemStack("categories." + category + "." + i);
         if (item != null) {
            inventory.setItem(i, item);
         }
      }

   }

   public void delete(String category) {
      if (this.exists(category)) {
         configManager.set("data/kitroom.yml", "categories." + category, (Object)null);
      }

      try {
         configManager.saveConfig("data/kitroom.yml");
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save category", e);
      }

   }

   public boolean exists(String category) {
      return configManager.contains("data/kitroom.yml", "categories." + category + ".");
   }
}
