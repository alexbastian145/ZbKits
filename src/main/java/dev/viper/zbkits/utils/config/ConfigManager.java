package dev.viper.zbkits.utils.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
   private static ConfigManager instance;
   private final Plugin plugin;
   private final Map<String, FileConfiguration> configurations = new ConcurrentHashMap<>();

   private ConfigManager(Plugin plugin) {
      this.plugin = plugin;
   }

   public static synchronized ConfigManager get(Plugin plugin) {
      if (instance == null) {
         instance = new ConfigManager(plugin);
      }

      return instance;
   }

   public synchronized FileConfiguration getConfig(String filePath) {
      if (!this.configurations.containsKey(filePath)) {
         this.create(filePath);
      }

      return (FileConfiguration)this.configurations.get(filePath);
   }

   public void saveConfig(String filePath) {
      File configFile = new File(this.plugin.getDataFolder(), filePath);
      FileConfiguration config = this.getConfig(filePath);

      try {
         // Write to a temp file first and move it into place, so a crash can't leave a half-written file.
         Path target = configFile.toPath();
         Path temp = target.resolveSibling(target.getFileName() + ".tmp");
         Files.writeString(temp, config.saveToString(), StandardCharsets.UTF_8);

         try {
            Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException e) {
         this.plugin.getLogger().warning("Could not save config file: " + filePath);
      }

   }

   public synchronized void reloadConfig(String filePath) {
      File configFile = new File(this.plugin.getDataFolder(), filePath);
      this.configurations.put(filePath, YamlConfiguration.loadConfiguration(configFile));
   }

   public synchronized void create(String filePath) {
      File configFile = new File(this.plugin.getDataFolder(), filePath);
      if (!configFile.exists()) {
         this.plugin.saveResource(filePath, false);
      }

      FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
      this.configurations.put(filePath, config);
   }

   public void set(String filePath, String path, Object value) {
      FileConfiguration config = this.getConfig(filePath);
      config.set(path, value);
   }

   public Object get(String filePath, String path) {
      FileConfiguration config = this.getConfig(filePath);
      return config.get(path);
   }

   public boolean contains(String filePath, String path) {
      FileConfiguration config = this.getConfig(filePath);
      return config.contains(path);
   }

   public Object getItemStack(String filePath, String path) {
      FileConfiguration config = this.getConfig(filePath);
      return config.getItemStack(path);
   }

   public void remove(String filePath, String path) {
      FileConfiguration config = this.getConfig(filePath);
      config.set(path, (Object)null);
   }
}
