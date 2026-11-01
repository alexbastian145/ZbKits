package dev.viper.utils.menu;

import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.menu.paginated.PaginatedSlot;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuUtils {
   public static int getCenter(int size) {
      return size / 2 + (size / 9 % 2 > 0 ? 1 : 5);
   }

   public static int getCenterSlot(int size) {
      return getCenter(size) - 1;
   }

   public static int paginatedSlotToSlot(@NotNull PaginatedSlot slot) {
      return slot.slot();
   }

   public static @NotNull PaginatedSlot slotToPaginatedSlot(int slot) {
      return PaginatedSlot.paginatedSlot(slot, 1);
   }

   public static int[] paginatedSlotToArray(@NotNull PaginatedSlot slot) {
      return new int[]{slot.slot(), slot.page()};
   }

   public static @NotNull List<ItemBuilder> contents(@NotNull MenuBase<?> menu) {
      List<ItemBuilder> result = new ArrayList();

      for(ItemStack stack : menu.inventory().getContents()) {
         ItemBuilder item = ItemBuilder.item(stack);
         result.add(item);
      }

      return result;
   }

   public static void clear(@NotNull MenuBase<?> menu) {
      menu.inventory().clear();
   }
}
