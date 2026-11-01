package dev.viper.utils.scheduler;

import dev.viper.utils.library.Utils;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SchedulerBase {
   public abstract void execute(@NotNull Consumer<BukkitTask> var1);

   public abstract void execute(@NotNull Runnable var1);

   public abstract void execute(@NotNull Consumer<BukkitTask> var1, int var2);

   public abstract void execute(@NotNull Runnable var1, int var2);

   public abstract void execute(@NotNull Consumer<BukkitTask> var1, int var2, int var3);

   public abstract void execute(@NotNull Runnable var1, int var2, int var3);

   public abstract @Nullable Object execute(@NotNull Supplier<?> var1);

   public abstract <T> @Nullable T supply(@NotNull Supplier<T> var1);

   public final @NotNull BukkitScheduler scheduler() {
      return Utils.plugin().getServer().getScheduler();
   }
}
