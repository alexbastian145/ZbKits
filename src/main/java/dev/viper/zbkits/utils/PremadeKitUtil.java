package dev.viper.zbkits.utils;

import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.text.color.ColorizeText;
import dev.viper.zbkits.api.PremadeKitAPI;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.zbkits.zbKits;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PremadeKitUtil implements PremadeKitAPI {
   private static final String FILE = "data/premadekit.yml";
   /** Guards the shared premade kit file; on Folia every player's region thread can reach it. */
   private static final Object LOCK = new Object();
   private static ConfigManager configManager;
   private final Logger logger = Logger.getLogger(PremadeKitUtil.class.getName());

   public PremadeKitUtil(ConfigManager configManager) {
      PremadeKitUtil.configManager = configManager;
   }

   public static void of(zbKits plugin) {
      configManager = ConfigManager.get(plugin);
      configManager.create(FILE);
   }

   public void save(Player player) {
      try {
         PlayerInventory inventory = player.getInventory();
         List<ItemStack> inventoryList = Arrays.asList(inventory.getContents());
         List<ItemStack> armorList = Arrays.asList(inventory.getArmorContents());
         ItemStack offhand = inventory.getItemInOffHand();
         synchronized(LOCK) {
            ConfigurationSection premadeKitSection = configManager.getConfig(FILE).createSection("premadekit");
            premadeKitSection.set("inventory", inventoryList);
            premadeKitSection.set("armor", armorList);
            premadeKitSection.set("offhand", offhand);
            configManager.saveConfig(FILE);
         }

         Messages.send(player, "premade_kit_saved");
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save premade kits file", e);
      }

   }

   public void set(GuiBuilder inventory) {
      List<ItemStack> inventoryItems;
      List<ItemStack> armorItems;
      ItemStack offhandItem;
      synchronized(LOCK) {
         ConfigurationSection section = configManager.getConfig(FILE).getConfigurationSection("premadekit");
         inventoryItems = section == null ? null : readItems(section, "inventory");
         armorItems = section == null ? null : readItems(section, "armor");
         offhandItem = section == null ? null : section.getItemStack("offhand");
      }

      if (inventoryItems != null) {
         for(int i = 0; i < inventoryItems.size() && i < 41; ++i) {
            ItemStack item = inventoryItems.get(i);
            if (item != null) {
               inventory.setItem(i, item);
            }
         }

         for(int i = 0; i < armorItems.size() && i < 4; ++i) {
            ItemStack item = armorItems.get(i);
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
      List<ItemStack> inventoryItems;
      List<ItemStack> armorItems;
      ItemStack offhandItem;
      synchronized(LOCK) {
         ConfigurationSection section = configManager.getConfig(FILE).getConfigurationSection("premadekit");
         inventoryItems = section == null ? new ArrayList<>() : readItems(section, "inventory");
         armorItems = section == null ? new ArrayList<>() : readItems(section, "armor");
         offhandItem = section == null ? null : section.getItemStack("offhand");
      }

      boolean empty = inventoryItems.stream().allMatch((item) -> item == null) && armorItems.stream().allMatch((item) -> item == null) && offhandItem == null;
      if (empty) {
         Messages.send(player, "premade_kit_empty");
      } else {
         PlayerInventory inventory = player.getInventory();
         inventory.clear();

         for(int i = 0; i < inventoryItems.size() && i < 41; ++i) {
            inventory.setItem(i, inventoryItems.get(i));
         }

         ItemStack[] armor = new ItemStack[4];

         for(int i = 0; i < armorItems.size() && i < 4; ++i) {
            armor[i] = armorItems.get(i);
         }

         inventory.setArmorContents(armor);
         if (offhandItem != null) {
            inventory.setItemInOffHand(offhandItem);
         }

         if (zbKits.getInstance().getConfig().getBoolean("broadcast.premadekit_load", true)) {
            String bcastLoaded = zbKits.getInstance().getConfig().getString("broadcast.premadekit_load_message");
            if (bcastLoaded != null) {
               bcastLoaded = bcastLoaded.replace("%player%", player.getName());
               Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
            }
         }

         Messages.send(player, "premade_kit_loaded");
      }

   }

   public void saveAll() {
      try {
         synchronized(LOCK) {
            configManager.saveConfig(FILE);
         }
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save premadekits file", e);
      }

   }

   /** Reads a saved item list. A missing, empty or damaged list gives an empty list; bad entries become null. */
   private static List<ItemStack> readItems(ConfigurationSection section, String key) {
      List<ItemStack> items = new ArrayList<>();
      List<?> raw = section.getList(key);
      if (raw != null) {
         for(Object entry : raw) {
            items.add(entry instanceof ItemStack stack ? stack : null);
         }
      }

      return items;
   }
}
