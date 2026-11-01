package dev.viper.utils.menu.xmenu;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PaginatedGuiBuilder extends GuiBuilder {
   private static final ItemStack DEFAULT_NEXT_PAGE_ITEM;
   private static final ItemStack DEFAULT_PREVIOUS_PAGE_ITEM;
   private final List<List<ItemStack>> pages;
   private final int itemsPerPage;
   private int currentPage;
   private int nextPageSlot;
   private int previousPageSlot;
   private ItemStack nextPageItem;
   private ItemStack previousPageItem;

   public PaginatedGuiBuilder(int size, String title) {
      this(size, title, size - 9, size - 6, size - 4, DEFAULT_PREVIOUS_PAGE_ITEM, DEFAULT_NEXT_PAGE_ITEM);
   }

   public PaginatedGuiBuilder(int size, String title, int itemsPerPage, int previousPageSlot, int nextPageSlot, ItemStack previousPageItem, ItemStack nextPageItem) {
      super(size, title);
      this.pages = new ArrayList();
      this.currentPage = 0;
      this.itemsPerPage = itemsPerPage;
      this.previousPageSlot = previousPageSlot;
      this.nextPageSlot = nextPageSlot;
      this.previousPageItem = previousPageItem;
      this.nextPageItem = nextPageItem;
      this.addNavigationButtons();
   }

   private static ItemStack createDefaultItem(Material material, String name) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(name);
         item.setItemMeta(meta);
      }

      return item;
   }

   private void addNavigationButtons() {
      this.setItem(this.previousPageSlot, this.previousPageItem, this::previousPage);
      this.setItem(this.nextPageSlot, this.nextPageItem, this::nextPage);
   }

   public void setNextPageItem(ItemStack item) {
      this.nextPageItem = item;
      this.setItem(this.nextPageSlot, this.nextPageItem, this::nextPage);
   }

   public void setPreviousPageItem(ItemStack item) {
      this.previousPageItem = item;
      this.setItem(this.previousPageSlot, this.previousPageItem, this::previousPage);
   }

   public void setNextPageSlot(int slot) {
      this.nextPageSlot = slot;
      this.setItem(this.nextPageSlot, this.nextPageItem, this::nextPage);
   }

   public void setPreviousPageSlot(int slot) {
      this.previousPageSlot = slot;
      this.setItem(this.previousPageSlot, this.previousPageItem, this::previousPage);
   }

   public void addItemToPage(ItemStack item, int page) {
      if (page < 0) {
         page = 0;
      } else if (page >= this.pages.size()) {
         for(int i = this.pages.size(); i <= page; ++i) {
            this.pages.add(new ArrayList());
         }
      }

      ((List)this.pages.get(page)).add(item);
      if (page == this.currentPage) {
         this.updatePage();
      }

   }

   public void addItemToCurrentPage(ItemStack item) {
      this.addItemToPage(item, this.currentPage);
   }

   public void nextPage(InventoryClickEvent event) {
      if (this.currentPage < this.pages.size() - 1) {
         ++this.currentPage;
         this.updatePage();
      }

   }

   public void previousPage(InventoryClickEvent event) {
      if (this.currentPage > 0) {
         --this.currentPage;
         this.updatePage();
      }

   }

   private void updatePage() {
      this.clearItems();
      List<ItemStack> items = (List)this.pages.get(this.currentPage);

      for(int i = 0; i < items.size(); ++i) {
         this.setItem(i, (ItemStack)items.get(i));
      }

   }

   private void clearItems() {
      for(int i = 0; i < this.itemsPerPage; ++i) {
         this.removeItem(i);
      }

   }

   public void open(Player player) {
      this.updatePage();
      super.open(player);
   }

   protected void onClick(InventoryClickEvent event) {
   }

   protected void onClose(InventoryCloseEvent event) {
   }

   protected void onOpen(InventoryOpenEvent event) {
   }

   static {
      DEFAULT_NEXT_PAGE_ITEM = createDefaultItem(Material.ARROW, "Next Page");
      DEFAULT_PREVIOUS_PAGE_ITEM = createDefaultItem(Material.ARROW, "Previous Page");
   }
}
