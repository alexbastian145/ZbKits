package dev.viper.utils.menu.listener;

import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.menu.Button;
import dev.viper.utils.menu.normal.Menu;
import dev.viper.utils.menu.paginated.PaginatedMenu;
import dev.viper.utils.menu.paginated.PaginatedSlot;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {
   @EventHandler
   public void onClick(InventoryClickEvent event) {
      if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
         Player player = (Player)event.getWhoClicked();
         InventoryHolder button = event.getInventory().getHolder();
         if (button instanceof Menu) {
            Menu menu = (Menu)button;
            Button button = menu.button(event.getSlot());
            if (button != null) {
               Button.ButtonListener listener = button.listener();
               if (listener != null) {
                  listener.onClick(event);
               }
            }
         }

         button = event.getInventory().getHolder();
         if (button instanceof PaginatedMenu) {
            PaginatedMenu menu = (PaginatedMenu)button;
            Button stickyButton = menu.stickyButton(event.getSlot());
            if (menu.currentPageItemEnabled() && event.getSlot() == menu.currentPageSlot()) {
               event.setCancelled(true);
            }

            for(Map.Entry<Integer, ItemBuilder> entry : menu.previousButton().entrySet()) {
               if (event.getSlot() == (Integer)entry.getKey()) {
                  event.setCancelled(true);
                  if (menu.currentPage() > 1) {
                     menu.open(player, menu.currentPage() - 1);
                  }
               }
            }

            for(Map.Entry<Integer, ItemBuilder> entry : menu.nextButton().entrySet()) {
               if (event.getSlot() == (Integer)entry.getKey()) {
                  event.setCancelled(true);
                  if (menu.totalPages() > 1) {
                     menu.open(player, menu.currentPage() + 1);
                  }
               }
            }

            for(Map.Entry<Integer, Button> entry : menu.stickyButtons().entrySet()) {
               if (event.getSlot() == (Integer)entry.getKey() && stickyButton != null && stickyButton.listener() != null) {
                  stickyButton.listener().onClick(event);
               }
            }

            for(Map.Entry<PaginatedSlot, Button> entry : menu.buttons().entrySet()) {
               PaginatedSlot slotHolder = (PaginatedSlot)entry.getKey();
               if (slotHolder.slot() == event.getSlot() && slotHolder.page() == menu.currentPage()) {
                  Button button = (Button)entry.getValue();
                  if (button != null && button.listener() != null) {
                     button.listener().onClick(event);
                  }
               }
            }
         }

      }
   }

   @EventHandler
   public void onClose(InventoryCloseEvent event) {
      Player player = (Player)event.getPlayer();
      InventoryHolder var5 = event.getInventory().getHolder();
      if (var5 instanceof Menu menu) {
         if (menu.onClose() != null) {
            menu.onClose().onClose(event);
         }
      } else {
         var5 = event.getInventory().getHolder();
         if (var5 instanceof PaginatedMenu menu) {
            if (menu.onClose() != null) {
               menu.onClose().onClose(event);
            }
         }
      }

   }

   @EventHandler
   public void onDrag(InventoryDragEvent event) {
      Player player = (Player)event.getWhoClicked();
      InventoryHolder var5 = event.getInventory().getHolder();
      if (var5 instanceof Menu menu) {
         if (menu.onDrag() != null) {
            menu.onDrag().onDrag(event);
         }
      } else {
         var5 = event.getInventory().getHolder();
         if (var5 instanceof PaginatedMenu menu) {
            if (menu.onDrag() != null) {
               menu.onDrag().onDrag(event);
            }
         }
      }

   }
}
