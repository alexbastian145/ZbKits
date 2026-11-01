package dev.viper.utils.config.setter;

import dev.viper.utils.config.Config;
import dev.viper.utils.config.section.ConfigSection;
import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.library.Utils;
import dev.viper.utils.text.Text;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigSectionSetter {
   private final ConfigSection configSection;
   private Object val;
   private String key;
   private boolean defaultVal;

   public ConfigSectionSetter(@NotNull ConfigSection configSection) {
      this.configSection = configSection;
      this.defaultVal = true;
   }

   public @NotNull ConfigSectionSetter key(@NotNull String key) {
      this.key = this.configSection.sectionPath + "." + key;
      return this;
   }

   public @NotNull ConfigSectionSetter val(@Nullable Object val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(@Nullable Text val) {
      if (val != null) {
         this.val = val.toString();
      }

      return this;
   }

   public @NotNull ConfigSectionSetter val(@Nullable Location val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(@Nullable ItemStack val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(@Nullable ItemBuilder val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(@Nullable List<?> val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(int val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(double val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(long val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(float val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter val(boolean val) {
      this.val = val;
      return this;
   }

   public @NotNull ConfigSectionSetter defaultVal(boolean defaultVal) {
      this.defaultVal = defaultVal;
      return this;
   }

   public @NotNull Config build() {
      if (this.key != null && this.val != null) {
         Utils.plugin().getConfig().set(this.key, this.val);
         if (this.defaultVal) {
            Utils.plugin().getConfig().addDefault(this.key, this.val);
         }

         return Config.config();
      } else {
         throw new IllegalArgumentException("key or val must not be null");
      }
   }
}
