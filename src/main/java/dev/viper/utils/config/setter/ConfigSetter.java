package dev.viper.utils.config.setter;

import dev.viper.utils.config.Config;
import dev.viper.utils.config.ConfigKey;
import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.library.Utils;
import dev.viper.utils.text.Text;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigSetter {
   private ConfigKey key;
   private Object val;
   private boolean defaultVal = false;

   public @NotNull ConfigSetter key(@NotNull ConfigKey key) {
      this.key = key;
      return this;
   }

   public @NotNull ConfigSetter val(@Nullable Object val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(@Nullable Text val) {
      if (val != null) {
         this.val = val.toString();
      }

      return this;
   }

   public @NotNull ConfigSetter val(@Nullable Location val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(@Nullable ItemStack val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(@Nullable ItemBuilder val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(@Nullable List<?> val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(int val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(double val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(long val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(float val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter val(boolean val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSetter defaultVal(boolean defaultVal) {
      this.defaultVal = defaultVal;
      return this;
   }

   public @NotNull Config build() {
      if (this.key != null && this.key.val != null && this.val != null) {
         Utils.plugin().getConfig().set(this.key.path, this.val);
         if (this.defaultVal) {
            Utils.plugin().getConfig().addDefault(this.key.path, this.val);
         }

         return Config.config();
      } else {
         throw new IllegalArgumentException("key or val must not be null");
      }
   }
}
