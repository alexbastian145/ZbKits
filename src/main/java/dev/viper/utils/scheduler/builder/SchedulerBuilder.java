package dev.viper.utils.scheduler.builder;

import dev.viper.utils.scheduler.Schedulers;
import dev.viper.utils.scheduler.builder.task.SchedulerTask;
import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SchedulerBuilder {
   private SchedulerTask schedulerTask;
   private final @NotNull SchedulerConfig config = new SchedulerConfig();

   public SchedulerBuilder() {
      this.schedulerTask = null;
   }

   public SchedulerBuilder(@NotNull Runnable runnable) {
      this.schedulerTask = SchedulerTask.wrap(runnable);
   }

   public SchedulerBuilder(@NotNull Consumer<BukkitTask> task) {
      this.schedulerTask = SchedulerTask.wrap(task);
   }

   public @NotNull SchedulerBuilder config(@NotNull Consumer<SchedulerConfig> configConsumer) {
      configConsumer.accept(this.config);
      return this;
   }

   public @NotNull SchedulerBuilder async() {
      this.config.async();
      return this;
   }

   public @NotNull SchedulerBuilder sync() {
      this.config.sync();
      return this;
   }

   public @NotNull SchedulerBuilder everyTicks(int everyTicks) {
      this.config.everyTicks(everyTicks);
      return this;
   }

   public @NotNull SchedulerBuilder afterTicks(int afterTicks) {
      this.config.afterTicks(afterTicks);
      return this;
   }

   public @NotNull SchedulerBuilder runnable(@NotNull Runnable runnable) {
      this.schedulerTask = SchedulerTask.wrap(runnable);
      return this;
   }

   public @NotNull SchedulerBuilder task(@NotNull Consumer<BukkitTask> task) {
      this.schedulerTask = SchedulerTask.wrap(task);
      return this;
   }

   public void execute() {
      switch (this.config.threadType()) {
         case SYNC:
            if (this.config.everyTicks() == null && this.config.afterTicks() == null) {
               Object var23 = this.schedulerTask.appropriate();
               if (var23 instanceof Consumer) {
                  Consumer<BukkitTask> consumer = (Consumer<BukkitTask>)var23;
                  Schedulers.sync().execute(consumer);
               } else {
                  var23 = this.schedulerTask.appropriate();
                  if (var23 instanceof Runnable) {
                     Runnable runnable = (Runnable)var23;
                     Schedulers.sync().execute(runnable);
                  }
               }
            } else if (this.config.everyTicks() == null && this.config.afterTicks() != null) {
               Object var21 = this.schedulerTask.appropriate();
               if (var21 instanceof Consumer) {
                  Consumer<BukkitTask> consumer = (Consumer<BukkitTask>)var21;
                  Schedulers.sync().execute(consumer, this.config.afterTicks());
               } else {
                  var21 = this.schedulerTask.appropriate();
                  if (var21 instanceof Runnable) {
                     Runnable runnable = (Runnable)var21;
                     Schedulers.sync().execute(runnable, this.config.afterTicks());
                  }
               }
            } else {
               if (this.config.everyTicks() == null || this.config.afterTicks() == null) {
                  throw new UnsupportedOperationException("Not supported");
               }

               Object var19 = this.schedulerTask.appropriate();
               if (var19 instanceof Consumer) {
                  Consumer<BukkitTask> consumer = (Consumer<BukkitTask>)var19;
                  Schedulers.sync().execute(consumer, this.config.afterTicks(), this.config.everyTicks());
               } else {
                  var19 = this.schedulerTask.appropriate();
                  if (var19 instanceof Runnable) {
                     Runnable runnable = (Runnable)var19;
                     Schedulers.sync().execute(runnable, this.config.afterTicks(), this.config.everyTicks());
                  }
               }
            }
            break;
         case ASYNC:
            if (this.config.everyTicks() == null && this.config.afterTicks() == null) {
               Object taskConsumer = this.schedulerTask.appropriate();
               if (taskConsumer instanceof Consumer) {
                  Consumer<BukkitTask> consumer = (Consumer<BukkitTask>)taskConsumer;
                  Schedulers.async().execute(consumer);
               } else {
                  taskConsumer = this.schedulerTask.appropriate();
                  if (taskConsumer instanceof Runnable) {
                     Runnable runnable = (Runnable)taskConsumer;
                     Schedulers.async().execute(runnable);
                  }
               }
            } else if (this.config.everyTicks() == null && this.config.afterTicks() != null) {
               Object taskConsumer = this.schedulerTask.appropriate();
               if (taskConsumer instanceof Consumer) {
                  Consumer<BukkitTask> consumer = (Consumer<BukkitTask>)taskConsumer;
                  Schedulers.async().execute(consumer, this.config.afterTicks());
               } else {
                  taskConsumer = this.schedulerTask.appropriate();
                  if (taskConsumer instanceof Runnable) {
                     Runnable runnable = (Runnable)taskConsumer;
                     Schedulers.async().execute(runnable, this.config.afterTicks());
                  }
               }
            } else {
               if (this.config.everyTicks() == null || this.config.afterTicks() == null) {
                  throw new UnsupportedOperationException("Not supported");
               }

               Object taskConsumer = this.schedulerTask.appropriate();
               if (taskConsumer instanceof Consumer) {
                  Consumer<BukkitTask> consumer = (Consumer<BukkitTask>)taskConsumer;
                  Schedulers.async().execute(consumer, this.config.afterTicks(), this.config.everyTicks());
               } else {
                  taskConsumer = this.schedulerTask.appropriate();
                  if (taskConsumer instanceof Runnable) {
                     Runnable runnable = (Runnable)taskConsumer;
                     Schedulers.async().execute(runnable, this.config.afterTicks(), this.config.everyTicks());
                  }
               }
            }
      }

   }
}
