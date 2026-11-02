package dev.viper.zbkits.listeners;

import dev.viper.zbkits.zbKits;
import dev.viper.utils.event.crystal.PlayerAnchorDeathEvent;
import dev.viper.utils.server.Servers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRekitAnchorListener implements Listener {
   @EventHandler
   public void onAnchorDeath(PlayerAnchorDeathEvent e) {
      Player killer = e.attacker();
      if (killer != null) {
         // Runs 5 ticks later on the killer's own scheduler (Folia-safe). The killer can have left
         // in the meantime, so never assume they are still online.
         killer.getScheduler().runDelayed(zbKits.getInstance(), (task) -> {
            if (killer.isOnline() && zbKits.getAutoRekitUtil().isEnabled(killer)) {
               AutoRekitListener.load(killer);
            }

         }, null, 5L);
      }

   }

   public void register(JavaPlugin plugin) {
      HandlerList.unregisterAll(this);
      Servers.server().getPluginManager().registerEvents(this, plugin);
   }
}
