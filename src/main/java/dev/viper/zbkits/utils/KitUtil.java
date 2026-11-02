package dev.viper.zbkits.utils;

import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.text.color.ColorizeText;
import dev.viper.zbkits.api.KitsAPI;
import dev.viper.zbkits.api.events.KitLoadEvent;
import dev.viper.zbkits.api.events.KitSaveEvent;
import dev.viper.zbkits.utils.data.PlayerDataManager;
import dev.viper.zbkits.zbKits;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Kit storage. Kits live in the player's own data file (see {@link PlayerDataManager}) under
 * "&lt;kit name&gt;.inventory.*", ".armor.*" and ".offhand". The ender chest saved for the same
 * kit sits next to it under ".enderchest" and is handled by {@link EnderChestUtil}.
 */
public class KitUtil implements KitsAPI {
   private final PlayerDataManager data;

   public KitUtil(PlayerDataManager data) {
      this.data = data;
   }

   public void save(Player player, String kitName) {
      Inventory top = player.getOpenInventory().getTopInventory();
      ItemStack[] inventoryContents = new ItemStack[36];

      for(int i = 0; i < 36; ++i) {
         inventoryContents[i] = top.getItem(i);
      }

      ItemStack[] armorContents = new ItemStack[4];

      for(int i = 36; i < 40; ++i) {
         armorContents[i - 36] = top.getItem(i);
      }

      ItemStack offhandItem = top.getItem(40);
      KitSaveEvent event = new KitSaveEvent(player, kitName, inventoryContents, armorContents, offhandItem);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         UUID id = player.getUniqueId();
         YamlConfiguration config = this.data.get(id);
         this.clearKit(config, kitName);

         for(int i = 0; i < 36; ++i) {
            config.set(kitName + ".inventory." + i, inventoryContents[i]);
         }

         for(int i = 0; i < 4; ++i) {
            config.set(kitName + ".armor." + i, armorContents[i]);
         }

         config.set(kitName + ".offhand", offhandItem);
         this.data.save(id, config);
         Messages.send(player, "kit_saved", "%kit%", kitName);
      }

   }

   public void load(Player player, String kitName) {
      KitLoadEvent event = new KitLoadEvent(player, kitName);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         YamlConfiguration config = this.data.get(player.getUniqueId());
         if (hasKit(config, kitName)) {
            for(int i = 0; i < 36; ++i) {
               player.getInventory().setItem(i, config.getItemStack(kitName + ".inventory." + i));
            }

            for(int i = 0; i < 4; ++i) {
               player.getInventory().setItem(36 + i, config.getItemStack(kitName + ".armor." + i));
            }

            player.getInventory().setItemInOffHand(config.getItemStack(kitName + ".offhand"));
            if (zbKits.getInstance().getConfig().getBoolean("broadcast.kit_load", true)) {
               String bcastLoaded = zbKits.getInstance().getConfig().getString("broadcast.kit_load_message");
               if (bcastLoaded != null) {
                  bcastLoaded = bcastLoaded.replace("%player%", player.getName()).replace("%kit%", kitName);
                  Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
               }
            }

            Messages.send(player, "kit_loaded", "%kit%", kitName);
            zbKits.getEnderChestUtil().load(player, kitName);
         } else {
            Messages.send(player, "kit_empty", "%kit%", kitName);
         }
      }

   }

   public void set(Player player, String kitName, GuiBuilder inventory) {
      this.set(player.getUniqueId(), kitName, inventory);
   }

   /** Fills a GUI with a kit of any player, online or not. */
   public void set(UUID playerId, String kitName, GuiBuilder inventory) {
      YamlConfiguration config = this.data.get(playerId);
      if (hasKit(config, kitName)) {
         for(int i = 0; i < 36; ++i) {
            inventory.setItem(i, config.getItemStack(kitName + ".inventory." + i));
         }

         for(int i = 0; i < 4; ++i) {
            inventory.setItem(36 + i, config.getItemStack(kitName + ".armor." + i));
         }

         inventory.setItem(40, config.getItemStack(kitName + ".offhand"));
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
      this.delete(player.getUniqueId(), kitName);
   }

   /** Deletes a kit of any player, online or not. The ender chest saved for the kit is not touched. */
   public void delete(UUID playerId, String kitName) {
      YamlConfiguration config = this.data.get(playerId);
      this.clearKit(config, kitName);
      this.data.save(playerId, config);
   }

   public boolean exists(Player player, String kitName) {
      return this.exists(player.getUniqueId(), kitName);
   }

   public boolean exists(UUID playerId, String kitName) {
      return hasKit(this.data.get(playerId), kitName);
   }

   /**
    * Kits are named "Kit 1", "Kit 2", ... and use the permission zbkits.kit1, zbkits.kit2, ...
    * Names that don't follow that pattern aren't restricted.
    */
   public boolean canUse(Player player, String kitName) {
      if (kitName != null && kitName.startsWith("Kit ")) {
         String number = kitName.substring(4).trim();
         if (!number.isEmpty() && number.chars().allMatch(Character::isDigit)) {
            return player.hasPermission("zbkits.kit" + number);
         }
      }

      return true;
   }

   public void saveAll() {
      this.data.flushPending();
   }

   private void clearKit(YamlConfiguration config, String kitName) {
      config.set(kitName + ".inventory", null);
      config.set(kitName + ".armor", null);
      config.set(kitName + ".offhand", null);
   }

   private static boolean hasKit(YamlConfiguration config, String kitName) {
      return config.contains(kitName + ".inventory") || config.contains(kitName + ".armor") || config.contains(kitName + ".offhand");
   }
}
