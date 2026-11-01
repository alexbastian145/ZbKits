package dev.viper.utils.scheduler.builder.task;

import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchedulerTask {
   private final @Nullable Runnable runnable;
   private final @Nullable Consumer<BukkitTask> task;

   public SchedulerTask() {
      this.runnable = null;
      this.task = null;
   }

   public SchedulerTask(@Nullable Runnable runnable) {
      this.runnable = runnable;
      this.task = null;
   }

   public SchedulerTask(@Nullable Consumer<BukkitTask> task) {
      this.task = task;
      this.runnable = null;
   }

   public static @NotNull SchedulerTask wrap(@Nullable Runnable runnable) {
      return new SchedulerTask(runnable);
   }

   public static @NotNull SchedulerTask wrap(@Nullable Consumer<BukkitTask> task) {
      return new SchedulerTask(task);
   }

   public @Nullable Runnable runnable() {
      return this.runnable;
   }

   public @Nullable Consumer<BukkitTask> task() {
      return this.task;
   }

   public @Nullable Object appropriate() {
      if (this.task == null && this.runnable != null) {
         return this.runnable;
      } else {
         return this.runnable == null && this.task != null ? this.task : null;
      }
   }
}
