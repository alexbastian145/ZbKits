package dev.viper.utils.resource;

import dev.viper.utils.resource.format.ResourceFormat;
import dev.viper.utils.resource.format.ResourceFormats;
import dev.viper.utils.resource.path.ResourcePath;
import dev.viper.utils.resource.path.ResourcePaths;
import java.util.function.Consumer;

public class ResourceBuilder {
   private ResourceFormat format = ResourceFormats.yaml();
   private final ResourceOptions options = new ResourceOptions();
   private ResourcePath path = ResourcePaths.plugin();
   private String name = null;

   public static ResourceBuilder builder() {
      return new ResourceBuilder();
   }

   public ResourceFormat format() {
      return this.format;
   }

   public ResourceBuilder format(ResourceFormat format) {
      this.format = format;
      return this;
   }

   public ResourceOptions options() {
      return this.options;
   }

   public ResourceBuilder options(Consumer<ResourceOptions> optionsConsumer) {
      optionsConsumer.accept(this.options);
      return this;
   }

   public String name() {
      return this.name;
   }

   public ResourceBuilder name(String name) {
      this.name = name;
      return this;
   }

   public ResourcePath path() {
      return this.path;
   }

   public ResourceBuilder path(ResourcePath path) {
      this.path = path;
      return this;
   }

   public ResourceFile build() {
      return ResourceFile.resource(this.name, this.format, this.options, this.path);
   }
}
