package dev.viper.zbkits.utils.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Stores each player's kits, ender chests and auto rekit settings in their own file
 * ({@code data/players/<uuid>.yml}).
 *
 * <ul>
 *   <li>Online players are cached in memory, so nothing is read from disk while they play.</li>
 *   <li>A save only rewrites the file of the player that changed.</li>
 *   <li>Thread safety: the YAML objects are only touched on the main thread. A save takes a text
 *       snapshot on the main thread and only that immutable String is written on the IO thread.</li>
 *   <li>Files are written to a temp file and moved into place, so a crash can't leave half a file.</li>
 * </ul>
 */
public class PlayerDataManager {
   private final Plugin plugin;
   private final File directory;
   private final Map<UUID, YamlConfiguration> cache = new ConcurrentHashMap<>();
   /** Snapshots that are waiting to be written (or are being written right now). */
   private final Map<UUID, String> pending = new ConcurrentHashMap<>();
   private final Object ioLock = new Object();
   private final ExecutorService io;

   public PlayerDataManager(Plugin plugin) {
      this.plugin = plugin;
      this.directory = new File(plugin.getDataFolder(), "data/players");
      if (!this.directory.isDirectory() && !this.directory.mkdirs()) {
         plugin.getLogger().warning("Could not create " + this.directory.getPath());
      }

      this.io = Executors.newSingleThreadExecutor((runnable) -> {
         Thread thread = new Thread(runnable, "zbKits-IO");
         thread.setDaemon(true);
         return thread;
      });
   }

   /**
    * Returns the data of a player. Online players are cached; for offline players a fresh copy is
    * loaded, which is only meant for short admin lookups. Call {@link #save} after changing it.
    * Must be called from the main thread.
    */
   public YamlConfiguration get(UUID id) {
      YamlConfiguration cached = this.cache.get(id);
      if (cached != null) {
         return cached;
      } else {
         YamlConfiguration loaded = this.read(id);
         if (Bukkit.getPlayer(id) != null) {
            YamlConfiguration existing = this.cache.putIfAbsent(id, loaded);
            return existing != null ? existing : loaded;
         } else {
            return loaded;
         }
      }
   }

   /** Queues a write of this player's data. Must be called from the main thread. */
   public void save(UUID id, YamlConfiguration config) {
      String snapshot = config.saveToString();
      if (this.pending.put(id, snapshot) == null) {
         this.schedule(id);
      }

   }

   /** Drops the in-memory copy of a player (their data is already queued for writing). */
   public void unload(UUID id) {
      this.cache.remove(id);
   }

   public boolean exists(UUID id) {
      return this.pending.containsKey(id) || this.file(id).isFile();
   }

   /** Writes a player file right now on the calling thread (used by the migration). */
   public boolean writeNow(UUID id, YamlConfiguration config) {
      synchronized(this.ioLock) {
         return this.writeFile(id, config.saveToString());
      }
   }

   /** Blocks until everything that was queued is on disk. */
   public void flushPending() {
      for(UUID id : new ArrayList<>(this.pending.keySet())) {
         synchronized(this.ioLock) {
            String data = this.pending.get(id);
            if (data != null && this.writeFile(id, data)) {
               this.pending.remove(id, data);
            }
         }
      }

   }

   /** Flushes everything and stops the IO thread. Call from onDisable. */
   public void shutdown() {
      this.io.shutdown();

      try {
         if (!this.io.awaitTermination(15L, TimeUnit.SECONDS)) {
            this.plugin.getLogger().warning("Timed out waiting for player data to finish saving.");
         }
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }

      this.flushPending();
      this.cache.clear();
   }

   private void schedule(UUID id) {
      try {
         this.io.execute(() -> this.flush(id));
      } catch (RejectedExecutionException e) {
         // The IO thread is already stopped (server shutting down): write on this thread instead.
         synchronized(this.ioLock) {
            String data = this.pending.get(id);
            if (data != null && this.writeFile(id, data)) {
               this.pending.remove(id, data);
            }
         }
      }

   }

   private void flush(UUID id) {
      synchronized(this.ioLock) {
         String data = this.pending.get(id);
         if (data == null) {
            return;
         }

         if (!this.writeFile(id, data)) {
            return;
         }

         this.pending.remove(id, data);
      }

      // A newer snapshot arrived while we were writing: write it as well.
      if (this.pending.containsKey(id)) {
         this.schedule(id);
      }

   }

   private boolean writeFile(UUID id, String data) {
      Path target = this.file(id).toPath();
      Path temp = target.resolveSibling(target.getFileName() + ".tmp");

      try {
         Files.writeString(temp, data, StandardCharsets.UTF_8);

         try {
            Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
         }

         return true;
      } catch (IOException e) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to save player data " + id, e);
         return false;
      }
   }

   private YamlConfiguration read(UUID id) {
      YamlConfiguration config = new YamlConfiguration();
      String snapshot = this.pending.get(id);

      try {
         if (snapshot != null) {
            config.loadFromString(snapshot);
         } else {
            File file = this.file(id);
            if (file.isFile()) {
               config.load(file);
            }
         }
      } catch (IOException e) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to read player data " + id, e);
      } catch (InvalidConfigurationException e) {
         this.plugin.getLogger().log(Level.SEVERE, "Player data " + id + " is corrupt, moving it aside", e);
         this.moveAside(this.file(id));
         return new YamlConfiguration();
      }

      return config;
   }

   private void moveAside(File file) {
      if (file.isFile()) {
         File aside = new File(file.getParentFile(), file.getName() + ".corrupt-" + System.currentTimeMillis());
         if (!file.renameTo(aside)) {
            this.plugin.getLogger().warning("Could not move corrupt file " + file.getName() + " aside.");
         }
      }

   }

   private File file(UUID id) {
      return new File(this.directory, id + ".yml");
   }
}
