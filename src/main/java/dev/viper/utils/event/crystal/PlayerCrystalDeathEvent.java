package dev.viper.utils.event.crystal;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerCrystalDeathEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final Player killer;
   private final Player victim;
   private final Entity crystal;
   private final EntityDamageByEntityEvent playerDamageEvent;
   private final PlayerDeathEvent playerDeathEvent;

   public PlayerCrystalDeathEvent(Player killer, Player victim, Entity crystal, EntityDamageByEntityEvent playerDamageEvent, PlayerDeathEvent playerDeathEvent) {
      this.killer = killer;
      this.victim = victim;
      this.crystal = crystal;
      this.playerDeathEvent = playerDeathEvent;
      this.playerDamageEvent = playerDamageEvent;
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
      return "PlayerCrystalDeathEvent";
   }

   public @NotNull String eventName() {
      return this.getEventName();
   }

   public void deathMessage(Component deathMessage) {
      this.playerDeathEvent.deathMessage(deathMessage);
   }

   public Component deathMessage() {
      return this.playerDeathEvent.deathMessage();
   }

   public Player killer() {
      return this.killer;
   }

   public Player victim() {
      return this.victim;
   }

   public Entity crystal() {
      return this.crystal;
   }

   public boolean keepInventory() {
      return this.playerDeathEvent.getKeepInventory();
   }

   public void keepInventory(boolean keepInventory) {
      this.playerDeathEvent.setKeepInventory(keepInventory);
   }

   public boolean keepLevel() {
      return this.playerDeathEvent.getKeepLevel();
   }

   public void keepLevel(boolean keepLevel) {
      this.playerDeathEvent.setKeepLevel(keepLevel);
   }

   public int newExp() {
      return this.playerDeathEvent.getNewExp();
   }

   public void newExp(int exp) {
      this.playerDeathEvent.setNewExp(exp);
   }

   public int newLevel() {
      return this.playerDeathEvent.getNewLevel();
   }

   public void newLevel(int level) {
      this.playerDeathEvent.setNewLevel(level);
   }

   public int newTotalExp() {
      return this.playerDeathEvent.getNewTotalExp();
   }

   public void newTotalExp(int totalExp) {
      this.playerDeathEvent.setNewTotalExp(totalExp);
   }

   public int droppedExp() {
      return this.playerDeathEvent.getDroppedExp();
   }

   public void droppedExp(int exp) {
      this.playerDeathEvent.setDroppedExp(exp);
   }

   public List<ItemStack> drops() {
      return this.playerDeathEvent.getDrops();
   }

   public double damage() {
      return this.playerDamageEvent.getDamage();
   }

   public final double finalDamage() {
      return this.playerDamageEvent.getFinalDamage();
   }

   public boolean isSuicide() {
      return this.killer == this.victim;
   }
}
