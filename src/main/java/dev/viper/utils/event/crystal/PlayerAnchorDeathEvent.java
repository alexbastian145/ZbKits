package dev.viper.utils.event.crystal;

import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerAnchorDeathEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final @NotNull BlockState block;
   private final @NotNull Player victim;
   private final @NotNull Player attacker;
   private final @Nullable RespawnAnchor anchor;

   public PlayerAnchorDeathEvent(@NotNull BlockState block, @NotNull Player victim, @NotNull Player attacker) {
      this.block = block;
      this.victim = victim;
      this.attacker = attacker;
      BlockState var5 = this.block;
      if (var5 instanceof RespawnAnchor respawnAnchor) {
         this.anchor = respawnAnchor;
      } else {
         this.anchor = null;
      }

   }

   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public @NotNull HandlerList getHandlers() {
      return HANDLERS;
   }

   public @NotNull HandlerList handlers() {
      return this.getHandlers();
   }

   public @NotNull String getEventName() {
      return "PlayerAnchorDeathEvent";
   }

   public @NotNull String eventName() {
      return this.getEventName();
   }

   public @NotNull BlockState block() {
      return this.block;
   }

   public @NotNull Player victim() {
      return this.victim;
   }

   public @NotNull Player attacker() {
      return this.attacker;
   }

   public @Nullable RespawnAnchor anchor() {
      return this.anchor;
   }
}
