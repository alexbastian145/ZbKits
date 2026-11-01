package dev.viper.utils.parser.menu;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.menu.Button;
import dev.viper.utils.menu.MenuBase;
import dev.viper.utils.menu.normal.Menu;
import dev.viper.utils.menu.paginated.PaginatedMenu;
import dev.viper.utils.model.Tuple;
import dev.viper.utils.parser.item.ItemParser;
import dev.viper.utils.text.color.TextStyle;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class MenuParser<M extends MenuBase<M>> {
   private final FileConfiguration configuration;
   private M menu;
   private final Class<M> menuType;

   public MenuParser(@NotNull FileConfiguration configuration, @NotNull Class<M> menuType) {
      this.configuration = configuration;
      this.menuType = menuType;
   }

   public static <M extends MenuBase<M>> @NotNull MenuParser<M> wrap(@NotNull FileConfiguration configuration, @NotNull Class<M> menuType) {
      return new MenuParser<M>(configuration, menuType);
   }

   public int size() {
      int size = this.configuration.getInt("menu.size");
      return size == 0 ? 54 : size;
   }

   public @NotNull Component title() {
      String raw = this.configuration.getString("menu.title");
      return (Component)(raw == null ? Component.text("Failed to load title") : TextStyle.style(raw));
   }

   public @NotNull ConfigurationSection itemsSection() {
      ConfigurationSection items = this.configuration.getConfigurationSection("items");
      return items == null ? this.configuration.createSection("items") : items;
   }

   public @NotNull ConfigurationSection customItemsSection() {
      ConfigurationSection items = this.configuration.getConfigurationSection("custom_items");
      return items == null ? this.configuration.createSection("custom_items") : items;
   }

   public @NotNull MenuParserItem normalItem(@NotNull String name) {
      ConfigurationSection section = this.itemsSection().getConfigurationSection(name);
      if (section == null) {
         return MenuParserItem.of(ItemBuilder.item(Material.AIR), List.of(10), false);
      } else {
         boolean enabled = section.getBoolean("enabled");
         List<Integer> slots = section.getIntegerList("slots");
         ItemBuilder builder = ItemParser.parse(this.configuration, "items." + name + ".item");
         return MenuParserItem.of(builder, slots, enabled);
      }
   }

   public @NotNull MenuParserItem customItem(@NotNull String name) {
      ConfigurationSection section = this.customItemsSection().getConfigurationSection(name);
      if (section == null) {
         return MenuParserItem.of(ItemBuilder.item(Material.AIR), List.of(10), false);
      } else {
         List<Integer> slots = section.getIntegerList("slots");
         ItemBuilder builder = ItemParser.parse(this.configuration, "custom_items." + name + ".item");
         return MenuParserItem.of(builder, slots, true);
      }
   }

   public void placeItem(@NotNull MenuParserItem item, Button.@NotNull ButtonListener listener) {
      List<Integer> slots = item.slots();
      boolean enabled = item.enabled();
      ItemBuilder builder = item.item();
      if (enabled) {
         for(int slot : slots) {
            this.menu().button(slot, Button.button().item(builder).onClick(listener));
         }

      }
   }

   public void placeItem(@NotNull MenuParserItem item) {
      this.placeItem(item, (event) -> event.setCancelled(true));
   }

   @CanIgnoreReturnValue
   public M menu() {
      if (this.menu == null) {
         if (this.menuType.isNestmateOf(PaginatedMenu.class)) {
            this.menu = (M)(this.menuType.cast(PaginatedMenu.menu(this.title(), this.size())));
         } else {
            this.menu = (M)(this.menuType.cast(Menu.menu(this.title(), this.size())));
         }
      }

      return this.menu;
   }

   public FileConfiguration configuration() {
      return this.configuration;
   }

   public @NotNull MenuParserItem normalItem(@NotNull String name, @NotNull List<Tuple<String, String>> replacements) {
      ConfigurationSection section = this.itemsSection().getConfigurationSection(name);
      if (section == null) {
         return MenuParserItem.of(ItemBuilder.item(Material.AIR), List.of(10), false);
      } else {
         boolean enabled = section.getBoolean("enabled");
         List<Integer> slots = section.getIntegerList("slots");
         ItemBuilder builder = ItemParser.parse(this.configuration, "items." + name + ".item", replacements);
         return MenuParserItem.of(builder, slots, enabled);
      }
   }

   public void placeCustomItems() {
      ConfigurationSection section = this.customItemsSection();

      for(String key : section.getKeys(false)) {
         if (section.isConfigurationSection(key)) {
            this.placeItem(this.customItem(key));
         }
      }

   }

   public @NotNull String placeholder(@NotNull String key) {
      return this.configuration.getString("menu.placeholders." + key, "???");
   }
}
