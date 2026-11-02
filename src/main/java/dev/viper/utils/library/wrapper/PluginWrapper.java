package dev.viper.utils.library.wrapper;

import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class PluginWrapper extends JavaPlugin implements Listener {
   protected void load() {
   }

   protected abstract void start();

   protected void stop() {
   }

   public final void onLoad() {
      this.load();
   }

   public final void onDisable() {
      this.stop();
   }

   public final void onEnable() {
      this.start();
   }

   public final void log(int priority, @NotNull String text) {
      if (priority >= 0 && priority < 300) {
         this.getLogger().log(Level.FINEST, text);
      } else if (priority >= 300 && priority < 400) {
         this.getLogger().log(Level.FINER, text);
      } else if (priority >= 400 && priority < 500) {
         this.getLogger().log(Level.FINE, text);
      } else if (priority >= 500 && priority < 700) {
         this.getLogger().log(Level.CONFIG, text);
      } else if (priority >= 700 && priority < 800) {
         this.getLogger().log(Level.INFO, text);
      } else if (priority >= 800 && priority < 900) {
         this.getLogger().log(Level.WARNING, text);
      } else {
         if (priority < 900 || priority >= 1000) {
            throw new IllegalArgumentException("Invalid priority integer: " + priority + ". Refer to the integer values of java.util.logging.Level");
         }

         this.getLogger().log(Level.SEVERE, text);
      }

   }

   public final void log(@NotNull Level level, @NotNull String text) {
      this.getLogger().log(level, text);
   }

   public final @NotNull Server server() {
      return this.getServer();
   }

   public final void disable() {
      this.plugins().disablePlugin(this);
   }

   public final @NotNull PluginManager plugins() {
      return this.server().getPluginManager();
   }
}
