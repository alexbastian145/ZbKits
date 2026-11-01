package dev.viper.utils.event.builder;

import dev.viper.utils.library.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EventBuilder<T extends Event> {
   final List<Object> actionList = new ArrayList();
   final Class<T> eventType;

   EventBuilder(@NotNull Class<T> eventType) {
      this.eventType = eventType;
   }

   public @NotNull EventBuilder<T> execute(@NotNull Consumer<T> consumer) {
      this.actionList.add(consumer);
      return this;
   }

   public static <T extends Event> @NotNull EventBuilder<T> event(@NotNull Class<T> eventType) {
      return new EventBuilder<T>(eventType);
   }

   public @NotNull EventHandler<T> build() {
      return new EventHandler<T>(this);
   }

   public @NotNull EventCallback<T> register(@NotNull JavaPlugin plugin) {
      return this.build().register(plugin);
   }

   public @NotNull EventCallback<T> register() {
      return this.build().register(Utils.plugin());
   }

   public List<Object> actionList() {
      return this.actionList;
   }

   public Class<T> eventType() {
      return this.eventType;
   }
}
