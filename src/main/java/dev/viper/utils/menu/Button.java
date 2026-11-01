package dev.viper.utils.menu;

import dev.viper.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Button {
   private ItemBuilder item;
   private ButtonListener listener;
   private boolean isRefreshingButton;
   private boolean isRefreshingAsync;
   private long refreshDelay;
   private long refreshPeriod;

   public Button() {
      this.item = new ItemBuilder(Material.AIR);
      this.isRefreshingButton = false;
      this.refreshDelay = 0L;
      this.refreshPeriod = 20L;
      this.isRefreshingAsync = true;
   }

   public static @NotNull Button button() {
      return new Button();
   }

   public static @NotNull Button button(@Nullable ItemBuilder item) {
      return (new Button()).item(item);
   }

   public static @NotNull Button button(@Nullable ItemBuilder item, @Nullable ButtonListener listener) {
      return (new Button()).item(item).onClick(listener);
   }

   public @NotNull Button refreshAsync(boolean refreshAsync) {
      this.isRefreshingAsync = refreshAsync;
      return this;
   }

   public @NotNull Button refresh(boolean refresh) {
      this.isRefreshingButton = refresh;
      return this;
   }

   public @NotNull Button refreshDelay(long refreshDelay) {
      this.refreshDelay = refreshDelay;
      return this;
   }

   public @NotNull Button refreshPeriod(long refreshPeriod) {
      this.refreshPeriod = refreshPeriod;
      return this;
   }

   public @NotNull Button refreshTime(long refreshDelay, long refreshPeriod) {
      this.refreshDelay = refreshDelay;
      this.refreshPeriod = refreshPeriod;
      return this;
   }

   public @NotNull Button onClick(@Nullable ButtonListener listener) {
      this.listener = listener;
      return this;
   }

   public @NotNull Button item(@Nullable ItemBuilder item) {
      this.item = item;
      return this;
   }

   public ItemBuilder item() {
      return this.item;
   }

   public ButtonListener listener() {
      return this.listener;
   }

   public boolean isRefreshingButton() {
      return this.isRefreshingButton;
   }

   public long refreshDelay() {
      return this.refreshDelay;
   }

   public long refreshPeriod() {
      return this.refreshPeriod;
   }

   public boolean isRefreshingAsync() {
      return this.isRefreshingAsync;
   }

   public interface ButtonListener {
      void onClick(@NotNull InventoryClickEvent var1);
   }
}
