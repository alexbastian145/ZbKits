package dev.viper.zbkits.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.api.KitsAPI;
import dev.viper.zbkits.api.events.KitLoadEvent;
import dev.viper.zbkits.api.events.KitSaveEvent;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.text.color.ColorizeText;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class KitUtil implements KitsAPI {
   private static ConfigManager configManager;
   private final Logger logger = Logger.getLogger(KitUtil.class.getName());

   public KitUtil(ConfigManager configManager) {
      KitUtil.configManager = configManager;
   }

   public static void of(JavaPlugin plugin) {
      configManager = ConfigManager.get(plugin);
      configManager.create("data/kits.yml");
   }

   public void save(final Player player, final String kitName) {
      final String playerName = player.getUniqueId().toString();
      final ItemStack[] inventoryContents = new ItemStack[36];

      for(int i = 0; i < 36; ++i) {
         inventoryContents[i] = player.getOpenInventory().getTopInventory().getItem(i);
      }

      final ItemStack[] armorContents = new ItemStack[4];

      for(int i = 36; i < 40; ++i) {
         armorContents[i - 36] = player.getOpenInventory().getTopInventory().getItem(i);
      }

      final ItemStack offhandItem = player.getOpenInventory().getTopInventory().getItem(40);
      KitSaveEvent event = new KitSaveEvent(player, kitName, inventoryContents, armorContents, offhandItem);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         (new BukkitRunnable() {
            public void run() {
               if (KitUtil.this.exists(player, kitName)) {
                  KitUtil.this.delete(player, kitName);
               }

               Map<String, Object> kitData = new HashMap();

               for(int i = 0; i < 36; ++i) {
                  kitData.put(playerName + "." + kitName + ".inventory." + i, inventoryContents[i]);
               }

               for(int i = 0; i < 4; ++i) {
                  kitData.put(playerName + "." + kitName + ".armor." + i, armorContents[i]);
               }

               kitData.put(playerName + "." + kitName + ".offhand", offhandItem);

               for(Map.Entry<String, Object> entry : kitData.entrySet()) {
                  KitUtil.configManager.set("data/kits.yml", (String)entry.getKey(), entry.getValue());
               }

               try {
                  KitUtil.configManager.saveConfig("data/kits.yml");
                  (new BukkitRunnable() {
                     public void run() {
                        String kitSaved = ((String)Objects.requireNonNull(zbKits.getInstance().getConfig().getString("messages.kit_saved"))).replace("%kit%", kitName);
                        player.sendMessage(ColorizeText.hex(kitSaved));
                     }
                  }).runTask(zbKits.getInstance());
               } catch (Exception e) {
                  KitUtil.this.logger.log(Level.SEVERE, "Failed to save kit: " + kitName, e);
               }

            }
         }).runTaskAsynchronously(zbKits.getInstance());
      }
   }

   public void load(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      KitLoadEvent event = new KitLoadEvent(player, kitName);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         if (configManager.contains("data/kits.yml", playerName + "." + kitName)) {
            for(int i = 0; i < 36; ++i) {
               ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".inventory." + i);
               player.getInventory().setItem(i, item);
            }

            for(int i = 0; i < 4; ++i) {
               ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".armor." + i);
               player.getInventory().setItem(36 + i, item);
            }

            ItemStack offhandItem = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".offhand");
            player.getInventory().setItemInOffHand(offhandItem);
            if (zbKits.getInstance().getConfig().getBoolean("broadcast.kit_load", true)) {
               String bcastLoaded = zbKits.getInstance().getConfig().getString("broadcast.kit_load_message");
               if (bcastLoaded != null) {
                  bcastLoaded = bcastLoaded.replace("%player%", player.getName()).replace("%kit%", kitName);
                  Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
               }
            }

            String kitLoaded = zbKits.getInstance().getConfig().getString("messages.kit_loaded");
            if (kitLoaded != null) {
               kitLoaded = kitLoaded.replace("%kit%", kitName);
               player.sendMessage(ColorizeText.hex(kitLoaded));
            }

            zbKits.getEnderChestUtil().load(player, kitName);
         } else {
            Messages.send(player, "kit_empty", "%kit%", kitName);
         }
      }

   }

   public void set(Player player, String kitName, GuiBuilder inventory) {
      String playerName = player.getUniqueId().toString();
      if (configManager.contains("data/kits.yml", playerName + "." + kitName)) {
         for(int i = 0; i < 36; ++i) {
            ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".inventory." + i);
            inventory.setItem(i, item);
         }

         for(int i = 0; i < 4; ++i) {
            ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".armor." + i);
            inventory.setItem(36 + i, item);
         }

         ItemStack offhandItem = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".offhand");
         inventory.setItem(40, offhandItem);
      }

   }

   public void importInventory(Player player, GuiBuilder inventory) {
      ItemStack[] playerItems = player.getInventory().getContents();

      for(int i = 0; i < 36; ++i) {
         inventory.setItem(i, playerItems[i]);
      }

      inventory.setItem(39, player.getInventory().getHelmet());
      inventory.setItem(38, player.getInventory().getChestplate());
      inventory.setItem(37, player.getInventory().getLeggings());
      inventory.setItem(36, player.getInventory().getBoots());
      inventory.setItem(40, player.getInventory().getItemInOffHand());
   }

   public void delete(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      configManager.set("data/kits.yml", playerName + "." + kitName, (Object)null);

      try {
         configManager.saveConfig("data/kits.yml");
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to delete kit: " + kitName, e);
      }

   }

   public boolean exists(Player player, String kitName) {
      String playerName = player.getUniqueId().toString();
      return configManager.contains("data/kits.yml", playerName + "." + kitName);
   }

   @CanIgnoreReturnValue
   public void saveAll() {
      try {
         configManager.saveConfig("data/kits.yml");
      } catch (Exception e) {
         this.logger.log(Level.SEVERE, "Failed to save kits file", e);
      }

   }
}
