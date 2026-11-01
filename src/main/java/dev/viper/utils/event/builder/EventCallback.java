package dev.viper.utils.event.builder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EventCallback<T extends Event> implements EventExecutor, Listener {
   public static final HandlerListCache HANDLER_LIST_CACHE = new HandlerListCache();
   private final Class<T> eventType;
   private final EventPriority eventPriority;
   private final boolean ignoredCancelled;
   private final Object[] handlerArray;
   private final AtomicBoolean isRegistered;
   private final JavaPlugin plugin;

   public EventCallback(@NotNull JavaPlugin plugin, @NotNull EventHandler<T> eventHandler) {
      this.plugin = plugin;
      this.eventType = eventHandler.eventType;
      this.eventPriority = eventHandler.eventPriority;
      this.ignoredCancelled = eventHandler.ignoreCancelled;
      this.handlerArray = new Object[eventHandler.actionList.size()];
      this.isRegistered = new AtomicBoolean();

      for(int i = 0; i < this.handlerArray.length; ++i) {
         this.handlerArray[i] = eventHandler.actionList.get(i);
      }

      this.register();
   }

   public boolean isRegistered() {
      return this.isRegistered.get();
   }

   public @NotNull EventPriority priority() {
      return this.eventPriority;
   }

   public boolean ignoreCancelled() {
      return this.ignoredCancelled;
   }

   public @NotNull Class<T> eventType() {
      return this.eventType;
   }

   public @NotNull JavaPlugin plugin() {
      return this.plugin;
   }

   public boolean unregister() {
      if (!this.isRegistered.getAndSet(false)) {
         return false;
      } else {
         HANDLER_LIST_CACHE.apply(this.eventType, this).unregister(this);
         return true;
      }
   }

   public void register() {
      if (!this.isRegistered.getAndSet(true)) {
         this.attemptRegistration();
      }
   }

   void attemptRegistration() {
      Bukkit.getPluginManager().registerEvent(this.eventType, this, this.eventPriority, this, this.plugin, this.ignoredCancelled);
   }

   public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
      if (this.eventType == event.getClass()) {
         if (!this.isRegistered.get()) {
            event.getHandlers().unregister(listener);
         } else {
            T eventCasted = (T)(this.eventType.cast(event));

            try {
               for(Object o : this.handlerArray) {
                  if (o != null && o instanceof Consumer) {
                     ((Consumer)o).accept(eventCasted);
                  }
               }

            } catch (ClassCastException exc) {
               throw new EventException(exc);
            }
         }
      }
   }

   public static class HandlerListCache implements BiFunction<Class<? extends Event>, EventCallback<?>, HandlerList> {
      public final Map<Class<? extends Event>, HandlerList> cacheMap = new HashMap();

      public HandlerList apply(Class<? extends Event> aClass, EventCallback<?> eventSubscription) {
         HandlerList handlerList = (HandlerList)this.cacheMap.get(aClass);
         if (handlerList == null) {
            try {
               handlerList = this.cache(aClass);
            } catch (ReflectiveOperationException e) {
               throw new RuntimeException(e);
            } finally {
               this.cacheMap.put(aClass, handlerList);
            }
         }

         return handlerList;
      }

      public HandlerList cache(Class<? extends Event> aClass) throws ReflectiveOperationException {
         Method method_getHandlerList = aClass.getMethod("getHandlerList");
         return (HandlerList)method_getHandlerList.invoke((Object)null);
      }
   }
}
