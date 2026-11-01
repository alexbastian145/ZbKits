package dev.viper.utils.scheduler;

import dev.viper.utils.scheduler.async.AsyncScheduler;
import dev.viper.utils.scheduler.builder.SchedulerBuilder;
import dev.viper.utils.scheduler.stacker.SchedulerStacker;
import dev.viper.utils.scheduler.sync.SyncScheduler;
import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Schedulers {
   public static @NotNull SyncScheduler sync() {
      return new SyncScheduler();
   }

   public static @NotNull AsyncScheduler async() {
      return new AsyncScheduler();
   }

   public static @NotNull SyncScheduler synchronous() {
      return sync();
   }

   public static @NotNull AsyncScheduler asynchronous() {
      return async();
   }

   public static @NotNull SchedulerBuilder builder() {
      return new SchedulerBuilder();
   }

   public static @NotNull SchedulerBuilder builder(@Nullable Runnable runnable) {
      return runnable != null ? new SchedulerBuilder(runnable) : new SchedulerBuilder();
   }

   public static @NotNull SchedulerBuilder builder(@Nullable Consumer<BukkitTask> task) {
      return task != null ? new SchedulerBuilder(task) : new SchedulerBuilder();
   }

   public static @NotNull SchedulerStacker stacker() {
      return new SchedulerStacker();
   }
}
