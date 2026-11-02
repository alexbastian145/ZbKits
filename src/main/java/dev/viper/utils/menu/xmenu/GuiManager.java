package dev.viper.utils.menu.xmenu;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public final class GuiManager {
   private static final AtomicBoolean REGISTERED = new AtomicBoolean(false);

   private GuiManager() {
      throw new UnsupportedOperationException();
   }

   public static void register(Plugin plugin) {
      Objects.requireNonNull(plugin, "plugin");
      if (REGISTERED.getAndSet(true)) {
         throw new IllegalStateException("XMenus is already registered");
      } else {
         Bukkit.getPluginManager().registerEvents(new InventoryListener(plugin), plugin);
      }
   }

   /**
    * Closes every open menu. On Folia a player may only be touched by the thread that owns them,
    * so players owned by another region are closed through their own scheduler.
    */
   public static void closeAll(Plugin plugin) {
      for(Player player : Bukkit.getOnlinePlayers()) {
         if (Bukkit.isOwnedByCurrentRegion(player)) {
            closeIfMenu(player);
         } else {
            player.getScheduler().execute(plugin, () -> closeIfMenu(player), null, 1L);
         }
      }

   }

   private static void closeIfMenu(Player player) {
      if (player.getOpenInventory().getTopInventory().getHolder() instanceof GuiBuilder) {
         player.closeInventory();
      }

   }

   public static final class InventoryListener implements Listener {
      private final Plugin plugin;

      public InventoryListener(Plugin plugin) {
         this.plugin = plugin;
      }

      @EventHandler
      public void onInventoryClick(InventoryClickEvent e) {
         Inventory clickedInventory = e.getClickedInventory();
         if (clickedInventory != null && e.getInventory().getHolder() instanceof GuiBuilder) {
            GuiBuilder inv = (GuiBuilder)e.getInventory().getHolder();
            if (clickedInventory.equals(e.getView().getTopInventory())) {
               e.setCancelled(true);
            }

            inv.handleClick(e);
         }

      }

      @EventHandler
      public void onInventoryOpen(InventoryOpenEvent e) {
         if (e.getInventory().getHolder() instanceof GuiBuilder) {
            GuiBuilder inv = (GuiBuilder)e.getInventory().getHolder();
            inv.handleOpen(e);
         }

      }

      @EventHandler
      public void onInventoryClose(InventoryCloseEvent e) {
         if (e.getInventory().getHolder() instanceof GuiBuilder) {
            GuiBuilder inv = (GuiBuilder)e.getInventory().getHolder();
            if (inv.handleClose(e)) {
               // Re-open on the next tick, on the scheduler of the player who owns the menu.
               Player viewer = (Player)e.getPlayer();
               viewer.getScheduler().run(this.plugin, (task) -> inv.open(viewer), null);
            }
         }

      }

      @EventHandler
      public void onPluginDisable(PluginDisableEvent e) {
         if (e.getPlugin() == this.plugin) {
            GuiManager.closeAll(this.plugin);
            GuiManager.REGISTERED.set(false);
         }

      }
   }
}
