package dev.viper.utils.resource.path;

import dev.viper.utils.library.Utils;

public class ResourcePath {
   private final String path;

   public ResourcePath(String path) {
      this.path = path;
   }

   public ResourcePath() {
      this.path = Utils.plugin().getDataFolder().getPath();
   }

   public static ResourcePath path(String path) {
      return new ResourcePath(path);
   }

   public String path() {
      return this.path;
   }
}
