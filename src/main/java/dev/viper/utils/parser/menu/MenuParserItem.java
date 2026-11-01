package dev.viper.utils.parser.menu;

import dev.viper.utils.item.ItemBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MenuParserItem {
   private final ItemBuilder item;
   private final boolean enabled;
   private final List<Integer> slots;

   public MenuParserItem(@NotNull ItemBuilder item, @NotNull List<Integer> slots) {
      this.item = item;
      this.enabled = true;
      this.slots = slots;
   }

   public MenuParserItem(@NotNull ItemBuilder item, @NotNull List<Integer> slots, boolean enabled) {
      this.item = item;
      this.enabled = enabled;
      this.slots = slots;
   }

   public static @NotNull MenuParserItem of(@NotNull ItemBuilder item, @NotNull List<Integer> slots) {
      return new MenuParserItem(item, slots);
   }

   public static @NotNull MenuParserItem of(@NotNull ItemBuilder item, @NotNull List<Integer> slots, boolean enabled) {
      return new MenuParserItem(item, slots, enabled);
   }

   public ItemBuilder item() {
      return this.item;
   }

   public boolean enabled() {
      return this.enabled;
   }

   public List<Integer> slots() {
      return this.slots;
   }
}
