package dev.viper.utils.config.section;

import dev.viper.utils.config.setter.ConfigSectionSetter;
import dev.viper.utils.library.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigSection {
   final ConfigurationSection section;
   public final String sectionPath;

   public ConfigSection(@NotNull String section) {
      if (Utils.plugin().getConfig().getConfigurationSection(section) == null) {
         Utils.plugin().getConfig().createSection(section);
      }

      this.section = Utils.plugin().getConfig().getConfigurationSection(section);
      this.sectionPath = section;
   }

   public static @NotNull ConfigSection section(@NotNull String section) {
      return new ConfigSection(section);
   }

   public @NotNull ConfigSectionSetter set() {
      return new ConfigSectionSetter(this);
   }

   public @Nullable Object get(@NotNull String key) {
      return this.section.get(this.sectionPath + "." + key);
   }

   public void delete() {
      Utils.plugin().getConfig().set(this.sectionPath, (Object)null);
   }
}
