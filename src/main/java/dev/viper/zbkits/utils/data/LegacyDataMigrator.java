package dev.viper.zbkits.utils.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * One-time import of the old single-file storage (data/kits.yml, data/enderchest.yml and
 * data/autorekit.yml, all players in one file) into one file per player.
 * The old files are renamed to *.migrated afterwards, never deleted.
 */
public final class LegacyDataMigrator {
   private static final String[] LEGACY_FILES = new String[]{"kits.yml", "enderchest.yml", "autorekit.yml"};

   private LegacyDataMigrator() {
   }

   public static void migrate(Plugin plugin, PlayerDataManager data) {
      File folder = new File(plugin.getDataFolder(), "data");
      Map<UUID, YamlConfiguration> merged = new HashMap<>();
      Map<File, Boolean> usable = new HashMap<>();

      for(String name : LEGACY_FILES) {
         File file = new File(folder, name);
         if (file.isFile() && file.length() != 0L) {
            YamlConfiguration old = new YamlConfiguration();

            try {
               old.load(file);
            } catch (InvalidConfigurationException | IOException e) {
               plugin.getLogger().log(Level.SEVERE, "Could not read legacy data file " + name + ", skipping migration of it", e);
               continue;
            }

            usable.put(file, true);

            for(String key : old.getKeys(false)) {
               UUID id;
               try {
                  id = UUID.fromString(key);
               } catch (IllegalArgumentException e) {
                  continue;
               }

               ConfigurationSection section = old.getConfigurationSection(key);
               if (section != null && !data.exists(id)) {
                  copy(section, merged.computeIfAbsent(id, (k) -> new YamlConfiguration()));
               }
            }
         }
      }

      if (!usable.isEmpty()) {
         boolean allWritten = true;

         for(Map.Entry<UUID, YamlConfiguration> entry : merged.entrySet()) {
            if (!data.writeNow(entry.getKey(), entry.getValue())) {
               allWritten = false;
            }
         }

         if (!allWritten) {
            plugin.getLogger().severe("Some players could not be migrated, the old data files were left in place and the migration will run again on next start.");
         } else {
            for(File file : usable.keySet()) {
               try {
                  Files.move(file.toPath(), new File(folder, file.getName() + ".migrated").toPath(), StandardCopyOption.REPLACE_EXISTING);
               } catch (IOException e) {
                  plugin.getLogger().log(Level.WARNING, "Could not rename " + file.getName(), e);
               }
            }

            plugin.getLogger().info("Migrated the data of " + merged.size() + " players to data/players/. Old files were renamed to *.migrated.");
         }
      }

   }

   private static void copy(ConfigurationSection from, ConfigurationSection to) {
      for(String path : from.getKeys(true)) {
         if (!from.isConfigurationSection(path)) {
            to.set(path, from.get(path));
         }
      }

   }
}
