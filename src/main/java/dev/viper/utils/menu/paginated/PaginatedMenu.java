package dev.viper.utils.menu.paginated;

import org.bukkit.scheduler.BukkitTask;
import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.library.Utils;
import dev.viper.utils.menu.Button;
import dev.viper.utils.menu.MenuBase;
import dev.viper.utils.menu.listener.CloseListener;
import dev.viper.utils.menu.listener.DragListener;
import dev.viper.utils.scheduler.Schedulers;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PaginatedMenu implements InventoryHolder, MenuBase<PaginatedMenu> {
   private final Inventory inventory;
   private final Component title;
   private final int size;
   private final Map<PaginatedSlot, Button> buttons;
   private final Map<PaginatedSlot, ItemBuilder> items;
   private int totalPages;
   private int currentPage;
   private final Map<Integer, ItemBuilder> previousButton;
   private final Map<Integer, ItemBuilder> nextButton;
   private final Map<Button, String[]> borderMap;
   private final Map<Integer, Button> stickyButtons;
   private boolean currentPageItemEnabled;
   private ItemBuilder currentPageItem;
   private int currentPageSlot;
   private CloseListener onClose;
   private DragListener onDrag;

   public PaginatedMenu(@NotNull Component title, int size) {
      this.inventory = Bukkit.createInventory(this, size, title);
      this.title = title;
      this.size = size;
      this.buttons = new HashMap();
      this.items = new HashMap();
      this.previousButton = new HashMap();
      this.currentPage = 1;
      this.nextButton = new HashMap();
      this.borderMap = new HashMap();
      this.stickyButtons = new HashMap();
   }

   public @NotNull Component title() {
      return this.title;
   }

   public int size() {
      return this.size;
   }

   public @NotNull Map<PaginatedSlot, Button> buttons() {
      return this.buttons;
   }

   public @NotNull Map<Integer, ItemBuilder> previousButton() {
      return this.previousButton;
   }

   public @NotNull Map<Integer, ItemBuilder> nextButton() {
      return this.nextButton;
   }

   public @NotNull Map<Button, String[]> borderMap() {
      return this.borderMap;
   }

   public @NotNull Map<Integer, Button> stickyButtons() {
      return this.stickyButtons;
   }

   public @NotNull Map<PaginatedSlot, ItemBuilder> items() {
      return this.items;
   }

   public int totalPages() {
      return this.totalPages;
   }

   public @NotNull PaginatedMenu totalPages(int totalPages) {
      this.totalPages = totalPages;
      return this;
   }

   public int currentPage() {
      return this.currentPage;
   }

   public @NotNull PaginatedMenu button(@NotNull PaginatedSlot where, @NotNull Button button) {
      this.buttons.put(where, button);
      return this;
   }

   public @NotNull PaginatedMenu item(@NotNull PaginatedSlot where, @Nullable ItemBuilder item) {
      this.items.put(where, item);
      return this;
   }

   public static @NotNull PaginatedMenu menu(@NotNull Component title, int size) {
      return new PaginatedMenu(title, size);
   }

   public static @NotNull PaginatedMenu menu(@NotNull Component title, int width, int height) {
      return menu(title, width * height);
   }

   public @NotNull PaginatedMenu stickyButton(int where, @NotNull Button button) {
      this.stickyButtons.put(where, button);
      return this;
   }

   public @NotNull PaginatedMenu paginationButtons(int previousItemSlot, @NotNull ItemBuilder previousItem, int nextItemSlot, @NotNull ItemBuilder nextItem) {
      this.inventory.setItem(previousItemSlot, previousItem.build());
      this.previousButton.put(previousItemSlot, previousItem);
      this.inventory.setItem(nextItemSlot, nextItem.build());
      this.nextButton.put(nextItemSlot, nextItem);
      return this;
   }

   public @NotNull PaginatedMenu currentPageButton(int slot, @NotNull ItemBuilder item) {
      this.currentPageItemEnabled = true;
      this.currentPageItem = item;
      this.currentPageSlot = slot;
      return this;
   }

   public @NotNull PaginatedMenu border(@NotNull Button borderItem, String... borderPatterns) {
      int row = 0;

      for(String borderPattern : borderPatterns) {
         if (row < this.size) {
            String[] rowCharacters = borderPattern.split(" ");

            for(int col = 0; col < rowCharacters.length && col < 9; ++col) {
               String character = rowCharacters[col];
               if (character.equals("X")) {
                  this.inventory.setItem(col + row * 9, borderItem.item().build().clone());
                  this.borderMap.put(borderItem, borderPatterns);
                  this.buttons.put(new PaginatedSlot(col + row * 9, this.currentPage), borderItem);
               }
            }

            ++row;
         }
      }

      return this;
   }

   public @NotNull PaginatedMenu fill(@NotNull Object filler, String... pattern) {
      int row = 0;

      for(String rowPattern : pattern) {
         if (row < this.size / 9) {
            String[] rowCharacters = rowPattern.split(" ");

            for(int col = 0; col < rowCharacters.length && col < 9; ++col) {
               String character = rowCharacters[col];
               int slot = col + row * 9;
               if (character.equals("X")) {
                  if (filler instanceof Button) {
                     this.button(new PaginatedSlot(slot, this.currentPage), (Button)filler);
                  } else if (filler instanceof ItemBuilder) {
                     this.item(new PaginatedSlot(slot, this.currentPage), (ItemBuilder)filler);
                  } else {
                     int callersLineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
                     Utils.plugin().getLogger().log(Level.WARNING, "You can not use PaginatedMenu.fill(Object filler, String... pattern) with a object different than an ItemBuilder or Button!");
                     Utils.plugin().getLogger().log(Level.WARNING, "    Line: [" + callersLineNumber + "], FileBuilder: [" + Thread.currentThread().getStackTrace()[2].getFileName() + "]");
                  }
               }
            }

            ++row;
         }
      }

      return this;
   }

   public @NotNull Map<Button, String[]> border() {
      return this.borderMap;
   }

   public void open(@NotNull Player player) {
      this.open(player, 1);
   }

   public void open(@NotNull Player player, int page) {
      this.inventory.clear();
      player.openInventory(this.inventory);
      this.currentPage = page;
      int highestPage = 1;

      for(PaginatedSlot slotHolder : this.buttons.keySet()) {
         highestPage = Math.max(highestPage, slotHolder.page());
         this.totalPages = highestPage;
      }

      for(PaginatedSlot slotHolder : this.items.keySet()) {
         if (this.totalPages > Math.max(highestPage, slotHolder.page())) {
            highestPage = Math.max(highestPage, slotHolder.page());
            this.totalPages = highestPage;
         }
      }

      for(Button button : this.buttons.values()) {
         if (this.pageSlotHolderByButton(button).page() == this.currentPage) {
            if (!button.isRefreshingButton()) {
               this.inventory.setItem(this.pageSlotHolderByButton(button).slot(), button.item().build());
            } else {
               Schedulers.builder((Consumer<BukkitTask>)((task) -> {
                  if (player.getOpenInventory().getTopInventory() != this.getInventory()) {
                     task.cancel();
                  } else if (this.pageSlotHolderByButton(button).page() != this.currentPage) {
                     task.cancel();
                  } else {
                     int slot = this.pageSlotHolderByButton(button).slot();
                     this.getInventory().clear(slot);
                     this.getInventory().setItem(slot, button.item().name(button.item().build().displayName()).build());
                     player.updateInventory();
                  }
               })).config((config) -> {
                  if (button.isRefreshingAsync()) {
                     config.async();
                  } else {
                     config.sync();
                  }

                  config.afterTicks((int)button.refreshDelay());
                  config.everyTicks((int)button.refreshPeriod());
               }).execute();
            }
         }
      }

      for(ItemBuilder item : this.items.values()) {
         if (this.pageSlotHolderByItem(item).page() == this.currentPage) {
            this.inventory.setItem(this.pageSlotHolderByItem(item).slot(), item.build());
         }
      }

      if (this.currentPage > 1) {
         for(Map.Entry<Integer, ItemBuilder> entry : this.previousButton.entrySet()) {
            this.inventory.setItem((Integer)entry.getKey(), ((ItemBuilder)this.previousButton.get(entry.getKey())).build());
         }
      }

      if (this.currentPage < this.totalPages) {
         for(Map.Entry<Integer, ItemBuilder> entry : this.nextButton.entrySet()) {
            this.inventory.setItem((Integer)entry.getKey(), ((ItemBuilder)this.nextButton.get(entry.getKey())).build());
         }
      }

      if (!this.borderMap.isEmpty()) {
         for(Map.Entry<Button, String[]> entry : this.borderMap.entrySet()) {
            this.border((Button)entry.getKey(), (String[])this.borderMap.get(entry.getKey()));
         }
      }

      for(Map.Entry<Integer, Button> entry : this.stickyButtons.entrySet()) {
         this.inventory.setItem((Integer)entry.getKey(), ((Button)this.stickyButtons.get(entry.getKey())).item().build());
      }

      if (this.totalPages > 1 && this.currentPageItemEnabled) {
         this.inventory.setItem(this.currentPageSlot, this.currentPageItem.rawName(this.currentPageItem.meta().getDisplayName().replaceAll("<current_page>", String.valueOf(this.currentPage)).replaceAll("<page>", String.valueOf(this.currentPage)).replaceAll("\\{current_page}", String.valueOf(this.currentPage)).replaceAll("\\{page}", String.valueOf(this.currentPage))).build());
      }

      player.updateInventory();
   }

   public void nextPage(@NotNull Player player) {
      this.open(player, this.currentPage() + 1);
   }

   public void previousPage(@NotNull Player player) {
      this.open(player, this.currentPage() - 1);
   }

   public @Nullable ItemBuilder item(@NotNull PaginatedSlot where) {
      return (ItemBuilder)this.items.get(where);
   }

   public @Nullable Button button(@NotNull PaginatedSlot where) {
      return (Button)this.buttons.get(where);
   }

   public @Nullable Button stickyButton(int where) {
      return (Button)this.stickyButtons.get(where);
   }

   public PaginatedSlot pageSlotHolderByButton(@NotNull Button button) {
      for(Map.Entry<PaginatedSlot, Button> entry : this.buttons.entrySet()) {
         if (entry.getValue() == button) {
            return (PaginatedSlot)entry.getKey();
         }
      }

      return null;
   }

   public PaginatedSlot pageSlotHolderByItem(@NotNull ItemBuilder item) {
      for(Map.Entry<PaginatedSlot, ItemBuilder> entry : this.items.entrySet()) {
         if (entry.getValue() == item) {
            return (PaginatedSlot)entry.getKey();
         }
      }

      return null;
   }

   public int slotByStickyButton(@NotNull Button button) {
      for(Map.Entry<Integer, Button> entry : this.stickyButtons.entrySet()) {
         if (entry.getValue() == button) {
            return (Integer)entry.getKey();
         }
      }

      return -1;
   }

   public int slotByNextPageButton(@NotNull ItemBuilder item) {
      for(Map.Entry<Integer, ItemBuilder> entry : this.nextButton.entrySet()) {
         if (entry.getValue() == item) {
            return (Integer)entry.getKey();
         }
      }

      return -1;
   }

   public int slotByPreviousPageButton(@NotNull ItemBuilder item) {
      for(Map.Entry<Integer, ItemBuilder> entry : this.previousButton.entrySet()) {
         if (entry.getValue() == item) {
            return (Integer)entry.getKey();
         }
      }

      return -1;
   }

   public @NotNull Inventory getInventory() {
      return this.inventory;
   }

   public @NotNull Inventory inventory() {
      return this.getInventory();
   }

   public @NotNull PaginatedMenu type() {
      return this;
   }

   public @NotNull PaginatedMenu onClose(@Nullable CloseListener onClose) {
      this.onClose = onClose;
      return this;
   }

   public @NotNull PaginatedMenu onDrag(@Nullable DragListener onDrag) {
      this.onDrag = onDrag;
      return this;
   }

   public CloseListener onClose() {
      return this.onClose;
   }

   public DragListener onDrag() {
      return this.onDrag;
   }

   public boolean currentPageItemEnabled() {
      return this.currentPageItemEnabled;
   }

   public ItemBuilder currentPageItem() {
      return this.currentPageItem;
   }

   public int currentPageSlot() {
      return this.currentPageSlot;
   }
}
