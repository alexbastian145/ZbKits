package dev.viper.utils.resource.format;

public class ResourceFormat {
   private String extension;

   public ResourceFormat(String extension) {
      this.extension = extension;
      if (!this.extension.contains(".")) {
         this.extension = "." + extension;
      }

   }

   public ResourceFormat() {
      this.extension = ".yml";
   }

   public static ResourceFormat format(String extension) {
      return new ResourceFormat(extension);
   }

   public String extension() {
      return this.extension;
   }
}
