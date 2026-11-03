package dev.viper.zbkits.listeners;

import dev.viper.zbkits.menus.KitRoomMenu;
import dev.viper.zbkits.zbKits;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/** Keeps the in-memory player data in step with who is online. */
public class PlayerDataListener implements Listener {
   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      // Drop anything left over from an earlier session and load the player's file fresh.
      zbKits.getPlayerData().unload(e.getPlayer().getUniqueId());
      zbKits.getPlayerData().get(e.getPlayer().getUniqueId());
   }

   /** Runs last, so other plugins' quit handlers can still use the data. Pending saves are already queued. */
   @EventHandler(priority = EventPriority.MONITOR)
   public void onQuit(PlayerQuitEvent e) {
      zbKits.getPlayerData().unload(e.getPlayer().getUniqueId());
      KitRoomMenu.setCurrentCategory(e.getPlayer(), null);
      AutoRekitListener.forget(e.getPlayer());
   }
}
