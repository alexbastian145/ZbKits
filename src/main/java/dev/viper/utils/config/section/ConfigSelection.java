package dev.viper.utils.config.section;

import dev.viper.utils.config.ConfigKey;
import dev.viper.utils.config.val.ConfigVal;
import dev.viper.utils.model.Tuple;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class ConfigSelection {
   final ConfigSection section;

   public ConfigSelection(@NotNull ConfigSection section) {
      this.section = section;
   }

   public static @NotNull ConfigSelection selection(@NotNull ConfigSection section) {
      return new ConfigSelection(section);
   }

   public void forEach(@NotNull Consumer<Tuple<ConfigKey, ConfigVal>> consumer) {
      if (this.section.section != null) {
         for(String key : this.section.section.getKeys(false)) {
            consumer.accept(Tuple.tuple(ConfigKey.key(key), ConfigKey.key(key).val()));
         }
      }

   }
}
