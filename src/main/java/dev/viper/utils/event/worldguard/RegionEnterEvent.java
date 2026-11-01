package dev.viper.utils.event.worldguard;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegionEnterEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final Player player;
   private final ProtectedRegion region;

   public RegionEnterEvent(Player player, ProtectedRegion region) {
      this.player = player;
      this.region = region;
   }

   public Player getPlayer() {
      return this.player;
   }

   public ProtectedRegion getRegion() {
      return this.region;
   }

   public String getRegionId() {
      return this.region.getId();
   }

   public @NotNull HandlerList getHandlers() {
      return HANDLERS;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS;
   }
}
