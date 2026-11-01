package dev.viper.utils.scheduler.sync;

import dev.viper.utils.library.Utils;
import dev.viper.utils.scheduler.SchedulerBase;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SyncScheduler extends SchedulerBase {
   public void execute(@NotNull Consumer<BukkitTask> task) {
      this.scheduler().runTask(Utils.plugin(), task);
   }

   public void execute(@NotNull Runnable runnable) {
      this.scheduler().runTask(Utils.plugin(), runnable);
   }

   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks) {
      this.scheduler().runTaskLater(Utils.plugin(), task, (long)afterTicks);
   }

   public void execute(@NotNull Runnable runnable, int afterTicks) {
      this.scheduler().runTaskLater(Utils.plugin(), runnable, (long)afterTicks);
   }

   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks, int everyTicks) {
      this.scheduler().runTaskTimer(Utils.plugin(), task, (long)afterTicks, (long)everyTicks);
   }

   public void execute(@NotNull Runnable runnable, int afterTicks, int everyTicks) {
      this.scheduler().runTaskTimer(Utils.plugin(), runnable, (long)afterTicks, (long)everyTicks);
   }

   public @Nullable Object execute(@NotNull Supplier<?> supplier) {
      return supplier.get();
   }

   public <T> @Nullable T supply(@NotNull Supplier<T> supplier) {
      return (T)supplier.get();
   }
}
