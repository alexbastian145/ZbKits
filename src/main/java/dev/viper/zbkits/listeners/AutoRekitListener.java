package dev.viper.zbkits.listeners;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.hooks.worldguard.WorldGuardHook;
import dev.viper.zbkits.utils.Messages;
import dev.viper.zbkits.utils.wg.BlacklistedRegion;
import dev.viper.utils.event.crystal.PlayerCrystalDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AutoRekitListener implements Listener {
   /** Reloads the player's chosen kit. Must run on the thread that owns the player. */
   public static void load(Player player) {
      if (!WorldGuardHook.get().isEnabled() || !BlacklistedRegion.isInBlacklistedRegion(player)) {
         String kit = zbKits.getAutoRekitUtil().getKit(player);
         if (zbKits.getKitUtil().canUse(player, kit)) {
            rekit(player, kit);
         }
      }
   }

   /**
    * Reloads the killer's kit on the killer's own scheduler. The death event runs on the victim's
    * thread, which on Folia is not necessarily the thread that owns the killer, so the killer's
    * inventory and data must never be touched from here directly.
    */
   public static void loadLater(Player attacker) {
      attacker.getScheduler().run(zbKits.getInstance(), (task) -> {
         if (attacker.isOnline() && zbKits.getAutoRekitUtil().isEnabled(attacker)) {
            load(attacker);
         }

      }, null);
   }

   private static void rekit(Player player, String kit) {
      zbKits.getKitUtil().load(player, kit);
      if (zbKits.getInstance().getConfig().getBoolean("messages.send_auto_rekit_message", true)) {
         Messages.send(player, "auto_rekit_message");
      }

   }

   @EventHandler
   public void onDeath(PlayerDeathEvent e) {
      Player attacker = e.getEntity().getKiller();
      if (attacker != null) {
         loadLater(attacker);
      }

   }

   @EventHandler
   public void onCrystalDeath(PlayerCrystalDeathEvent e) {
      Player attacker = e.victim().getKiller();
      if (attacker != null) {
         loadLater(attacker);
      }

   }
}
