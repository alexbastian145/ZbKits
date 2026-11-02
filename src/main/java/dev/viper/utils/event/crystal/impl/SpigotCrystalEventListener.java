package dev.viper.utils.event.crystal.impl;

import dev.viper.utils.event.crystal.PlayerCrystalDeathEvent;
import dev.viper.utils.library.Utils;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SpigotCrystalEventListener implements Listener {
   /**
    * The crystal kill that is currently being put together from the crystal hit, the victim's
    * damage and the victim's death. The three events of one kill run on the same thread, but on
    * Folia different regions run on different threads at the same time, so every thread gets its
    * own state instead of sharing fields between kills.
    */
   private static final class Kill {
      private Entity crystal;
      private Player victim;
      private Player killer;
      private EntityDamageByEntityEvent playerDamageEvent;

      private void reset() {
         this.crystal = null;
         this.victim = null;
         this.killer = null;
         this.playerDamageEvent = null;
      }
   }

   private final ThreadLocal<Kill> state = ThreadLocal.withInitial(Kill::new);

   @EventHandler
   public void onCrystalExplode(EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof EnderCrystal) {
         if (event.getDamager().getType() == EntityType.PLAYER) {
            Kill kill = this.state.get();
            kill.reset();
            kill.crystal = event.getEntity();
            kill.killer = (Player)event.getDamager();
         }
      }
   }

   @EventHandler
   public void onPlayerDamage(EntityDamageByEntityEvent event) {
      if (event.getEntityType() == EntityType.PLAYER) {
         Kill kill = this.state.get();
         if (kill.crystal == null) {
            kill.reset();
         } else if (event.getDamager() != kill.crystal) {
            kill.reset();
         } else {
            kill.victim = (Player)event.getEntity();
            kill.playerDamageEvent = event;
         }
      }
   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event) {
      Kill kill = this.state.get();
      if (kill.crystal != null && kill.killer != null && kill.victim != null && kill.playerDamageEvent != null) {
         if (event.getEntity() == kill.victim) {
            PlayerCrystalDeathEvent calledEvent = new PlayerCrystalDeathEvent(kill.killer, kill.victim, kill.crystal, kill.playerDamageEvent, event);
            kill.reset();
            Utils.plugin().getServer().getPluginManager().callEvent(calledEvent);
         }
      }
   }
}
