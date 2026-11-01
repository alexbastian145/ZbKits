package dev.viper.utils.menu.paginated;

import org.jetbrains.annotations.NotNull;

public record PaginatedSlot(int slot, int page) {
   public static @NotNull PaginatedSlot paginatedSlot(int slot, int page) {
      return new PaginatedSlot(slot, page);
   }
}
