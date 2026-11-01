package dev.viper.utils.resource;

import dev.viper.utils.library.Utils;
import dev.viper.utils.resource.format.ResourceFormat;
import dev.viper.utils.resource.format.ResourceFormats;
import dev.viper.utils.resource.path.ResourcePath;
import dev.viper.utils.resource.yaml.ResourceConfiguration;
import dev.viper.utils.scheduler.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ResourceFile {
   private ResourceFormat resourceFormat;
   private final ResourceOptions recourseOptions;
   private final ResourcePath resourcePath;
   private final String name;

   public ResourceFile(String name, ResourceFormat format, ResourceOptions options, ResourcePath path) {
      this.resourceFormat = format;
      this.recourseOptions = options;
      this.resourcePath = path;
      this.name = name;
      if (!this.resourceFormat.extension().contains(".")) {
         this.resourceFormat = ResourceFormat.format("." + this.resourceFormat.extension());
      }

   }

   public static ResourceFile resource(String name, ResourceFormat format, ResourceOptions options, ResourcePath path) {
      return new ResourceFile(name, format, options, path);
   }

   public static ResourceBuilder builder() {
      return ResourceBuilder.builder();
   }

   public ResourceConfiguration yaml() {
      return new ResourceConfiguration(this);
   }

   public FileConfiguration loadYml() {
      return (FileConfiguration)Schedulers.async().supply(() -> YamlConfiguration.loadConfiguration(this.file()));
   }

   public FileConfiguration reloadYml() {
      return this.loadYml();
   }

   public void saveYml() {
      if (!Objects.equals(this.format(), ResourceFormats.yaml()) && !Objects.equals(this.format(), ResourceFormats.yml())) {
         throw new UnsupportedOperationException("only yaml files are supported for saveOrLoad()");
      } else {
         Schedulers.async().execute((Consumer)((task) -> {
            try {
               this.file().mkdirs();
               this.file().createNewFile();
               FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file());
               configuration.save(this.file());
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
         }));
      }
   }

   public ResourceFile saveOrLoadYml() {
      if (!Objects.equals(this.format(), ResourceFormats.yaml()) && !Objects.equals(this.format(), ResourceFormats.yml())) {
         throw new UnsupportedOperationException("only yaml files are supported for saveOrLoad()");
      } else {
         Schedulers.async().execute((Consumer)((task) -> {
            if (this.file().exists()) {
               FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file());
            } else {
               JavaPlugin var10000 = Utils.plugin();
               String var10001 = this.name;
               if (var10000.getResource(var10001 + this.resourceFormat.extension()) != null) {
                  var10000 = Utils.plugin();
                  var10001 = this.resourcePath.path().replaceAll(Utils.plugin().getDataFolder().getPath(), "");
                  var10000.saveResource(var10001 + this.name + this.resourceFormat.extension(), false);
               }

               try {
                  if (!this.file().createNewFile()) {
                     Utils.plugin().getLogger().severe("Failed to create resource: " + this.file().getPath());
                     task.cancel();
                  }
               } catch (IOException e) {
                  throw new RuntimeException(e);
               }

               FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file());
               configuration.options().copyDefaults(true).parseComments(true).pathSeparator('.');
               if (this.recourseOptions.header() != null) {
                  configuration.options().setHeader(this.recourseOptions.header());
               }

               if (this.recourseOptions.footer() != null) {
                  configuration.options().setFooter(this.recourseOptions.footer());
               }

               try {
                  configuration.save(this.file());
               } catch (IOException e) {
                  throw new RuntimeException(e);
               }
            }
         }));
         return this;
      }
   }

   public File file() {
      File var10002 = new File(this.resourcePath.path());
      String var10003 = this.name;
      return new File(var10002, var10003 + this.resourceFormat.extension());
   }

   public ResourceFormat format() {
      return this.resourceFormat;
   }

   public ResourceOptions options() {
      return this.recourseOptions;
   }

   public ResourcePath path() {
      return this.resourcePath;
   }

   public String name() {
      return this.name;
   }
}
