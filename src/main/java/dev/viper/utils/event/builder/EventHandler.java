package dev.viper.utils.event.builder;

import com.google.common.collect.ImmutableList;
import dev.viper.utils.library.Utils;
import java.util.List;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EventHandler<T extends Event> {
   final List<Object> actionList;
   final Class<T> eventType;
   EventPriority eventPriority;
   boolean ignoreCancelled;

   EventHandler(@NotNull EventBuilder<T> builder) {
      if (builder.actionList.isEmpty()) {
         throw new UnsupportedOperationException("No actions defined");
      } else {
         this.actionList = ImmutableList.copyOf(builder.actionList);
         this.eventType = builder.eventType;
         this.eventPriority = EventPriority.NORMAL;
      }
   }

   public @NotNull EventCallback<T> register(@NotNull JavaPlugin plugin) {
      return new EventCallback<T>(plugin, this);
   }

   public @NotNull EventCallback<T> register() {
      return new EventCallback<T>(Utils.plugin(), this);
   }

   public @NotNull EventHandler<T> ignoreCancelled(boolean ignoreCancelled) {
      this.ignoreCancelled = ignoreCancelled;
      return this;
   }

   public @NotNull EventHandler<T> priority(@NotNull EventPriority eventPriority) {
      this.eventPriority = eventPriority;
      return this;
   }

   public @NotNull EventHandler<T> priority(int eventPriority) {
      EventHandler var10000;
      switch (eventPriority) {
         case 1:
         case 10:
         case 100:
         case 1000:
            var10000 = this.priority(EventPriority.MONITOR);
            break;
         case 2:
         case 20:
         case 200:
         case 2000:
            var10000 = this.priority(EventPriority.HIGHEST);
            break;
         case 3:
         case 30:
         case 300:
         case 3000:
            var10000 = this.priority(EventPriority.HIGH);
            break;
         case 4:
         case 40:
         case 400:
         case 4000:
            var10000 = this.priority(EventPriority.NORMAL);
            break;
         case 5:
         case 50:
         case 500:
         case 5000:
            var10000 = this.priority(EventPriority.LOW);
            break;
         case 6:
         case 60:
         case 600:
         case 6000:
            var10000 = this.priority(EventPriority.LOWEST);
            break;
         default:
            var10000 = this;
      }

      return var10000;
   }
}
