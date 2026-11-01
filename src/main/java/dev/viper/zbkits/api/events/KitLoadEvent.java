package dev.viper.zbkits.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class KitLoadEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final String kitName;
   private boolean cancelled = false;

   public KitLoadEvent(Player player, String kitName) {
      this.player = player;
      this.kitName = kitName;
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

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancel) {
      this.cancelled = cancel;
   }

   public @NotNull HandlerList getHandlers() {
      return handlers;
   }
}
