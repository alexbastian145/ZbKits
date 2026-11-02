package dev.viper.zbkits.listeners;

import dev.viper.zbkits.zbKits;
import dev.viper.utils.event.crystal.PlayerAnchorDeathEvent;
import dev.viper.utils.scheduler.Schedulers;
import dev.viper.utils.server.Servers;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRekitAnchorListener implements Listener {
   @EventHandler
   public void onAnchorDeath(PlayerAnchorDeathEvent e) {
      Schedulers.sync().execute((Consumer)((task) -> {
         // The killer can have left during the 5 tick delay, so never assume they are still online.
         Player killer = e.attacker();
         if (killer != null && killer.isOnline() && zbKits.getAutoRekitUtil().isEnabled(killer)) {
            AutoRekitListener.load(killer);
         }

      }), 5);
   }

   public void register(JavaPlugin plugin) {
      HandlerList.unregisterAll(this);
      Servers.server().getPluginManager().registerEvents(this, plugin);
   }
}
