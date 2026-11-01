package dev.viper.utils.menu;

import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.menu.listener.CloseListener;
import dev.viper.utils.menu.listener.DragListener;
import dev.viper.utils.menu.paginated.PaginatedSlot;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuBase<T> {
   @NotNull T type();

   @NotNull T onClose(@Nullable CloseListener var1);

   @NotNull T onDrag(@Nullable DragListener var1);

   default @NotNull T button(int slot, Button button) {
      return (T)this.type();
   }

   default @NotNull T item(int slot, ItemBuilder item) {
      return (T)this.type();
   }

   int size();

   Component title();

   void open(Player var1);

   @NotNull T border(Button var1, String... var2);

   @NotNull T fill(Object var1, String... var2);

   Inventory inventory();

   default @NotNull T button(PaginatedSlot where, Button button) {
      return (T)this.type();
   }

   default @NotNull T item(PaginatedSlot where, ItemBuilder item) {
      return (T)this.type();
   }

   default void open(Player player, int page) {
      this.open(player);
   }
}
