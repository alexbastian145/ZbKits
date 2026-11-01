package dev.viper.utils.event;

import dev.viper.utils.library.Utils;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class EventListener<T extends Event> implements Listener, EventExecutor {
   private final Class<T> clazz;
   private final EventPriority priority;
   private final boolean ignoreCancelled;
   private T event;

   public EventListener(@NotNull Class<T> clazz) {
      this(clazz, EventPriority.NORMAL);
   }

   public EventListener(@NotNull Class<T> clazz, @NotNull EventPriority priority) {
      this(clazz, priority, true);
   }

   public EventListener(@NotNull Class<T> clazz, @NotNull EventPriority priority, boolean ignoreCancelled) {
      this.clazz = clazz;
      this.priority = priority;
      this.ignoreCancelled = ignoreCancelled;
   }

   public final void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
      if (!this.clazz.isInstance(event)) {
         throw new EventException("Field `Class<T> clazz` must be an instance of `org.bukkit.event.Event`");
      } else {
         this.event = (T)(this.clazz.cast(event));
         this.execute(this.event());
      }
   }

   protected abstract void execute(@NotNull T var1);

   public final void register() {
      this.register(Utils.plugin());
   }

   public final void register(@NotNull JavaPlugin plugin) {
      plugin.getServer().getPluginManager().registerEvent(this.clazz, this, this.priority, this, plugin, this.ignoreCancelled);
   }

   public @NotNull Class<T> clazz() {
      return this.clazz;
   }

   public @NotNull EventPriority priority() {
      return this.priority;
   }

   public boolean ignoreCancelled() {
      return this.ignoreCancelled;
   }

   public @NotNull T event() {
      return this.event;
   }
}
