package dev.viper.utils.config.val;

import dev.viper.utils.config.ConfigKey;
import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.location.LocationUtils;
import dev.viper.utils.text.Text;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigVal {
   private final ConfigKey key;
   private final Object object;
   final String path;

   public ConfigVal(ConfigKey key) {
      this.key = key;
      this.object = key.val;
      this.path = key.path;
   }

   public static @NotNull ConfigVal val(@NotNull ConfigKey key) {
      return new ConfigVal(key);
   }

   public @Nullable Location asLocation() {
      return LocationUtils.deserialize((String)this.object);
   }

   public @Nullable ItemStack asItemStack() {
      return ItemStack.deserializeBytes(((ItemStack)this.object).serializeAsBytes());
   }

   public @Nullable ItemBuilder asItemBuilder() {
      return ItemBuilder.item(this.asItemStack());
   }

   public @Nullable Text asText() {
      return Text.text((String)this.object);
   }

   public @Nullable UUID asUUID() {
      return UUID.fromString((String)this.object);
   }

   public int asInt() {
      return Integer.parseInt((String)this.object);
   }

   public double asDouble() {
      return Double.parseDouble((String)this.object);
   }

   public float asFloat() {
      return Float.parseFloat((String)this.object);
   }

   public boolean asBoolean() {
      return Boolean.parseBoolean((String)this.object);
   }

   public @Nullable Material asMaterial() {
      return Material.matchMaterial((String)this.object);
   }

   public @Nullable ConfigList asListOf() {
      return ConfigList.list(this);
   }

   public @Nullable Object raw() {
      return this.object;
   }

   public <T> @Nullable T as(@NotNull Class<T> type) {
      return (T)(type == Component.class ? this.asText().component() : this.object);
   }

   public ConfigKey key() {
      return this.key;
   }
}
