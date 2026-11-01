package dev.viper.zbkits.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.api.PremadeKitAPI;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.text.color.ColorizeText;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PremadeKitUtil implements PremadeKitAPI {
   private static ConfigManager configManager;
   private final Logger logger = Logger.getLogger(PremadeKitUtil.class.getName());

   public PremadeKitUtil(ConfigManager configManager) {
      PremadeKitUtil.configManager = configManager;
   }

   public static void of(zbKits plugin) {
      configManager = ConfigManager.get(plugin);
      configManager.create("data/premadekit.yml");
   }

   public void save(Player player) {
      try {
         Inventory inventory = player.getInventory();
         List<ItemStack> inventoryList = Arrays.asList(inventory.getContents());
         List<ItemStack> armorList = Arrays.asList(player.getInventory().getArmorContents());
         ConfigurationSection premadeKitSection = configManager.getConfig("data/premadekit.yml").createSection("premadekit");
         premadeKitSection.set("inventory", inventoryList);
         premadeKitSection.set("armor", armorList);
         premadeKitSection.set("offhand", player.getInventory().getItemInOffHand());
         configManager.saveConfig("data/premadekit.yml");
         String saved = zbKits.getInstance().getConfig().getString("messages.premade_kit_saved");
         if (saved != null) {
            player.sendMessage(ColorizeText.hex(saved));
         }
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save premade kits file", e);
      }

   }

   public void set(GuiBuilder inventory) {
      ConfigurationSection premadeKitSection = configManager.getConfig("data/premadekit.yml").getConfigurationSection("premadekit");
      if (premadeKitSection != null) {
         List<ItemStack> inventoryItems = (List)premadeKitSection.get("inventory");
         List<ItemStack> armorItems = (List)premadeKitSection.get("armor");
         ItemStack offhandItem = premadeKitSection.getItemStack("offhand");

         for(int i = 0; i < ((List)Objects.requireNonNull(inventoryItems)).size(); ++i) {
            ItemStack item = (ItemStack)inventoryItems.get(i);
            if (item != null) {
               inventory.setItem(i, item);
            }
         }

         for(int i = 0; i < ((List)Objects.requireNonNull(armorItems)).size(); ++i) {
            ItemStack item = (ItemStack)armorItems.get(i);
            if (item != null) {
               inventory.setItem(i + 36, item);
            }
         }

         if (offhandItem != null) {
            inventory.setItem(40, offhandItem);
         }
      }

   }

   public void load(Player player) {
      ConfigurationSection premadeKitSection = configManager.getConfig("data/premadekit.yml").getConfigurationSection("premadekit");
      if (premadeKitSection != null) {
         player.getInventory().clear();
         player.getInventory().setContents((ItemStack[])((List)Objects.requireNonNull(premadeKitSection.get("inventory"))).toArray(new ItemStack[0]));
         player.getInventory().setArmorContents((ItemStack[])((List)Objects.requireNonNull(premadeKitSection.get("armor"))).toArray(new ItemStack[0]));
         ItemStack offhandItem = premadeKitSection.getItemStack("offhand");
         if (offhandItem != null) {
            player.getInventory().setItemInOffHand(offhandItem);
         }

         if (zbKits.getInstance().getConfig().getBoolean("broadcast.premadekit_load", true)) {
            String bcastLoaded = zbKits.getInstance().getConfig().getString("broadcast.premadekit_load_message");
            if (bcastLoaded != null) {
               bcastLoaded = bcastLoaded.replace("%player%", player.getName());
               Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
            }
         }

         String loaded = zbKits.getInstance().getConfig().getString("messages.premade_kit_loaded");
         if (loaded != null) {
            player.sendMessage(ColorizeText.hex(loaded));
         }
      } else {
         String empty = zbKits.getInstance().getConfig().getString("messages.premade_kit_empty");
         if (empty != null) {
            player.sendMessage(ColorizeText.hex(empty));
         }
      }

   }

   @CanIgnoreReturnValue
   public void saveAll() {
      try {
         configManager.saveConfig("data/premadekit.yml");
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save premadekits file", e);
      }

   }
}
