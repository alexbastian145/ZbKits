package dev.viper.utils.resource.path;

import dev.viper.utils.library.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public class ResourcePaths {
   public static ResourcePath plugin() {
      return ResourcePath.path(Utils.plugin().getDataFolder().getPath());
   }

   public static ResourcePath plugin(JavaPlugin plugin) {
      return ResourcePath.path(plugin.getDataFolder().getPath());
   }

   public static ResourcePath root() {
      return ResourcePath.path(Utils.plugin().getDataFolder().getPath().replaceAll("plugins/" + Utils.plugin().getName() + "/", ""));
   }
}
