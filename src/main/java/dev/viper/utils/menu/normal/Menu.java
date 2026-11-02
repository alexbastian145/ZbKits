package dev.viper.utils.menu.normal;

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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Menu implements InventoryHolder, MenuBase<Menu> {
   private final Inventory inventory;
   private final Component title;
   private final int size;
   private final Map<Integer, Button> buttons;
   private final Map<Integer, ItemBuilder> items;
   private CloseListener onClose;
   private DragListener onDrag;

   public Menu(@NotNull Component title, int size) {
      this.inventory = Utils.plugin().getServer().createInventory(this, size, title);
      this.title = title;
      this.size = size;
      this.buttons = new HashMap();
      this.items = new HashMap();
   }

   public @NotNull Menu button(int slot, @NotNull Button button) {
      this.buttons.put(slot, button);
      this.inventory.setItem(slot, button.item().build());
      return this;
   }

   public @NotNull Menu item(int slot, @NotNull ItemBuilder item) {
      this.items.put(slot, item);
      this.inventory.setItem(slot, item.build());
      return this;
   }

   public static @NotNull Menu menu(@NotNull Component title, int size) {
      return new Menu(title, size);
   }

   public static @NotNull Menu menu(@NotNull Component title, int width, int height) {
      return menu(title, width * height);
   }

   public int size() {
      return this.size;
   }

   public @NotNull Component title() {
      return this.title;
   }

   public void open(@NotNull Player player) {
      player.openInventory(this.inventory);

      for(Button button : this.buttons.values()) {
         if (button.isRefreshingButton()) {
            Schedulers.builder((Consumer<BukkitTask>)((task) -> {
               if (player.getOpenInventory().getTopInventory() != this.getInventory()) {
                  task.cancel();
               } else {
                  int slot = this.slotByButton(button);
                  this.getInventory().clear(slot);
                  this.getInventory().setItem(slot, button.item().build());
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

   public @NotNull Menu border(@NotNull Button borderItem, String... borderPatterns) {
      int row = 0;

      for(String borderPattern : borderPatterns) {
         if (row < this.size) {
            String[] rowCharacters = borderPattern.split(" ");

            for(int col = 0; col < rowCharacters.length && col < 9; ++col) {
               String character = rowCharacters[col];
               if (character.equals("X")) {
                  this.inventory.setItem(col + row * 9, borderItem.item().build().clone());
                  this.buttons.put(col + row * 9, borderItem);
               }
            }

            ++row;
         }
      }

      return this;
   }

   public @NotNull Menu fill(@NotNull Object filler, String... pattern) {
      int row = 0;

      for(String rowPattern : pattern) {
         if (row < this.size / 9) {
            String[] rowCharacters = rowPattern.split(" ");

            for(int col = 0; col < rowCharacters.length && col < 9; ++col) {
               String character = rowCharacters[col];
               int slot = col + row * 9;
               if (character.equals("X")) {
                  if (filler instanceof Button) {
                     this.button(slot, (Button)filler);
                  } else if (filler instanceof ItemBuilder) {
                     this.item(slot, (ItemBuilder)filler);
                  } else {
                     int callersLineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
                     Utils.plugin().getLogger().log(Level.WARNING, "You can not use Menu.fill(Object filler, String... pattern) with a object different than an ItemBuilder or Button!");
                     Utils.plugin().getLogger().log(Level.WARNING, "    Line: [" + callersLineNumber + "], FileBuilder: [" + Thread.currentThread().getStackTrace()[2].getFileName() + "]");
                  }
               }
            }

            ++row;
         }
      }

      return this;
   }

   public @Nullable ItemBuilder item(int slot) {
      return (ItemBuilder)this.items.get(slot);
   }

   public @Nullable Button button(int slot) {
      return (Button)this.buttons.get(slot);
   }

   public int slotByButton(@NotNull Button button) {
      for(Map.Entry<Integer, Button> entry : this.buttons.entrySet()) {
         if (entry.getValue() == button) {
            return (Integer)entry.getKey();
         }
      }

      return -1;
   }

   public int slotByItem(@NotNull ItemBuilder item) {
      for(Map.Entry<Integer, ItemBuilder> entry : this.items.entrySet()) {
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

   public @NotNull Menu type() {
      return this;
   }

   public @NotNull Menu onClose(@Nullable CloseListener onClose) {
      this.onClose = onClose;
      return this;
   }

   public @NotNull Menu onDrag(@Nullable DragListener onDrag) {
      this.onDrag = onDrag;
      return this;
   }

   public CloseListener onClose() {
      return this.onClose;
   }

   public DragListener onDrag() {
      return this.onDrag;
   }
}
