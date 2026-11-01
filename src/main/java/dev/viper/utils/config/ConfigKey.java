package dev.viper.utils.config;

import dev.viper.utils.config.val.ConfigVal;
import dev.viper.utils.library.Utils;
import org.jetbrains.annotations.NotNull;

public class ConfigKey {
   public final Object val;
   public final String path;

   public ConfigKey(@NotNull String key) {
      this.val = Utils.plugin().getConfig().get(key);
      this.path = key;
   }

   public static ConfigKey key(@NotNull String key) {
      return new ConfigKey(key);
   }

   public @NotNull ConfigVal val() {
      return ConfigVal.val(this);
   }
}
