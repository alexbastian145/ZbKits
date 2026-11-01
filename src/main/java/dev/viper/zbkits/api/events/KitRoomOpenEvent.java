package dev.viper.zbkits.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class KitRoomOpenEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;

   public KitRoomOpenEvent(Player player) {
      this.player = player;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public Player getPlayer() {
      return this.player;
   }

   public @NotNull HandlerList getHandlers() {
      return handlers;
   }
}
