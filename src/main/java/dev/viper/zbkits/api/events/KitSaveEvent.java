package dev.viper.zbkits.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KitSaveEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final String kitName;
   private final ItemStack[] inventoryContents;
   private final ItemStack[] armorContents;
   private final ItemStack offhandItem;
   private boolean cancelled;

   public KitSaveEvent(Player player, String kitName, ItemStack[] inventoryContents, ItemStack[] armorContents, ItemStack offhandItem) {
      this.player = player;
      this.kitName = kitName;
      this.inventoryContents = inventoryContents;
      this.armorContents = armorContents;
      this.offhandItem = offhandItem;
      this.cancelled = false;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getKitName() {
      return this.kitName;
   }

   public ItemStack[] getInventoryContents() {
      return this.inventoryContents;
   }

   public ItemStack[] getArmorContents() {
      return this.armorContents;
   }

   public ItemStack getOffhandItem() {
      return this.offhandItem;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public @NotNull HandlerList getHandlers() {
      return handlers;
   }
}
