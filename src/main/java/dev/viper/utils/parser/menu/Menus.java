package dev.viper.utils.parser.menu;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.viper.utils.library.Utils;
import dev.viper.utils.menu.MenuBase;
import java.io.File;
import java.util.Arrays;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class Menus {
   public static @NotNull FileConfiguration load(String name) {
      File menusFolder = new File(Utils.plugin().getDataFolder(), "menus");
      if (!menusFolder.exists()) {
         menusFolder.mkdirs();
      }

      File configFile = new File(menusFolder, name + ".yml");
      return YamlConfiguration.loadConfiguration(configFile);
   }

   @CanIgnoreReturnValue
   public static <T extends MenuBase<T>, M extends ParsedMenu<T>> @NotNull M menu(@NotNull Class<M> type, Object... parameters) {
      try {
         Class<?>[] parameterTypes = (Class[])Arrays.stream(parameters).map(Object::getClass).toArray((x$0) -> new Class[x$0]);
         return (M)(type.getDeclaredConstructor(parameterTypes).newInstance(parameters));
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
