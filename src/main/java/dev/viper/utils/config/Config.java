package dev.viper.utils.config;

import dev.viper.utils.config.section.ConfigSection;
import dev.viper.utils.config.section.ConfigSelection;
import dev.viper.utils.config.setter.ConfigSetter;
import dev.viper.utils.library.Utils;
import dev.viper.utils.server.Servers;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Config {
   public static @NotNull Config config() {
      return new Config();
   }

   public static void init() {
      File file = new File(Servers.dataFolder(), "config.yml");
      if (!file.exists()) {
         Utils.plugin().saveDefaultConfig();
         Utils.plugin().reloadConfig();
      }
   }

   public static @NotNull ConfigKey key(@NotNull String key) {
      return ConfigKey.key(key);
   }

   public static @NotNull ConfigSelection selection(@NotNull ConfigSection section) {
      return ConfigSelection.selection(section);
   }

   public static void save() {
      Utils.plugin().saveConfig();
   }

   public static @NotNull ConfigSetter set() {
      return new ConfigSetter();
   }

   public static void set(@NotNull ConfigKey key, @Nullable Object object) {
      set(key, object, false);
   }

   public static void set(@NotNull ConfigKey key, @Nullable Object object, boolean defaultVal) {
      (new ConfigSetter()).key(key).val(object).defaultVal(defaultVal);
   }

   public static void delete(@NotNull ConfigKey key) {
      Utils.plugin().getConfig().set(key.path, (Object)null);
   }

   public static ConfigSection section(@NotNull String section) {
      return ConfigSection.section(section);
   }

   public static void delete(@NotNull ConfigSection section) {
      section.delete();
   }

   public static void reload() {
      Utils.plugin().reloadConfig();
   }

   @NotNull Config a() {
      return this;
   }
}
