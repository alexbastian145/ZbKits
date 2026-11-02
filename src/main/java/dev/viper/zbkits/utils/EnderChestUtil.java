package dev.viper.zbkits.utils;

import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.zbkits.api.EnderChestAPI;
import dev.viper.zbkits.utils.data.PlayerDataManager;
import dev.viper.zbkits.zbKits;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * The ender chest saved for a kit. It is stored in the player's data file next to the kit,
 * under "&lt;kit name&gt;.enderchest.&lt;slot&gt;".
 */
public class EnderChestUtil implements EnderChestAPI {
   private static final int SIZE = 27;
   private final PlayerDataManager data;

   public EnderChestUtil(PlayerDataManager data) {
      this.data = data;
   }

   public void save(Player player, String kitName) {
      UUID id = player.getUniqueId();
      YamlConfiguration config = this.data.get(id);
      Inventory top = player.getOpenInventory().getTopInventory();
      // The section is always created, so a kit saved with an empty ender chest still counts as saved.
      config.set(path(kitName), null);
      ConfigurationSection section = config.createSection(path(kitName));

      for(int i = 0; i < SIZE; ++i) {
         ItemStack item = top.getItem(i);
         if (item != null) {
            section.set(String.valueOf(i), item);
         }
      }

      this.data.save(id, config);
      Messages.send(player, "enderchest_saved", "%kit%", kitName);
   }

   /**
    * Replaces the player's real ender chest with the one saved for the kit. Does nothing if
    * enderchest.load_with_kit is false or if no ender chest was saved for the kit.
    */
   public void load(Player player, String kitName) {
      if (zbKits.getInstance().getConfig().getBoolean("enderchest.load_with_kit", true)) {
         ConfigurationSection section = this.data.get(player.getUniqueId()).getConfigurationSection(path(kitName));
         if (section != null) {
            ItemStack[] incoming = new ItemStack[SIZE];

            for(String key : section.getKeys(false)) {
               try {
                  int slot = Integer.parseInt(key);
                  if (slot >= 0 && slot < SIZE) {
                     incoming[slot] = section.getItemStack(key);
                  }
               } catch (NumberFormatException ignored) {
               }
            }

            Inventory enderChest = player.getEnderChest();
            ItemStack[] current = enderChest.getContents();
            boolean hadItems = false;

            for(ItemStack item : current) {
               if (item != null && item.getType() != Material.AIR) {
                  hadItems = true;
                  break;
               }
            }

            boolean changes = !Arrays.equals(current, incoming);
            enderChest.setContents(incoming);
            if (hadItems && changes) {
               Messages.send(player, "enderchest_overwritten", "%kit%", kitName);
            }

         }
      }
   }

   public void set(Player player, String kitName, GuiBuilder inventory) {
      this.set(player.getUniqueId(), kitName, inventory);
   }

   /** Fills a GUI with the ender chest of a kit of any player, online or not. */
   public void set(UUID playerId, String kitName, GuiBuilder inventory) {
      ConfigurationSection section = this.data.get(playerId).getConfigurationSection(path(kitName));
      if (section != null) {
         for(String key : section.getKeys(false)) {
            ItemStack item = section.getItemStack(key);
            if (item != null) {
               try {
                  inventory.setItem(Integer.parseInt(key), item);
               } catch (NumberFormatException ignored) {
               }
            }
         }
      }

   }

   public void delete(Player player, String kitName) {
      this.delete(player.getUniqueId(), kitName);
   }

   public void delete(UUID playerId, String kitName) {
      YamlConfiguration config = this.data.get(playerId);
      config.set(path(kitName), null);
      this.data.save(playerId, config);
   }

   public boolean exists(Player player, String kitName) {
      return this.exists(player.getUniqueId(), kitName);
   }

   public boolean exists(UUID playerId, String kitName) {
      return this.data.get(playerId).contains(path(kitName));
   }

   public void saveAll() {
      this.data.flushPending();
   }

   private static String path(String kitName) {
      return kitName + ".enderchest";
   }
}
