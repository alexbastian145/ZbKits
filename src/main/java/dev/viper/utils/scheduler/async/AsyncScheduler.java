package dev.viper.utils.scheduler.async;

import dev.viper.utils.library.Utils;
import dev.viper.utils.scheduler.SchedulerBase;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AsyncScheduler extends SchedulerBase {
   public void execute(@NotNull Consumer<BukkitTask> task) {
      this.scheduler().runTaskAsynchronously(Utils.plugin(), task);
   }

   public void execute(@NotNull Runnable runnable) {
      this.scheduler().runTaskAsynchronously(Utils.plugin(), runnable);
   }

   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks) {
      this.scheduler().runTaskLaterAsynchronously(Utils.plugin(), task, (long)afterTicks);
   }

   public void execute(@NotNull Runnable runnable, int afterTicks) {
      this.scheduler().runTaskLaterAsynchronously(Utils.plugin(), runnable, (long)afterTicks);
   }

   public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks, int everyTicks) {
      this.scheduler().runTaskTimerAsynchronously(Utils.plugin(), task, (long)afterTicks, (long)everyTicks);
   }

   public void execute(@NotNull Runnable runnable, int afterTicks, int everyTicks) {
      this.scheduler().runTaskTimerAsynchronously(Utils.plugin(), runnable, (long)afterTicks, (long)everyTicks);
   }

   public @Nullable Object execute(@NotNull Supplier<?> supplier) {
      try {
         return CompletableFuture.supplyAsync(supplier).get();
      } catch (ExecutionException | InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

   public <T> @Nullable T supply(@NotNull Supplier<T> supplier) {
      try {
         return (T)CompletableFuture.supplyAsync(supplier).get();
      } catch (ExecutionException | InterruptedException e) {
         throw new RuntimeException(e);
      }
   }
}
