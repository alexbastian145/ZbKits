package dev.viper.utils.scheduler.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SchedulerConfig {
   private @Nullable Integer everyTicks = null;
   private @Nullable Integer afterTicks = null;
   private @NotNull SchedulerThreadType threadType;

   public SchedulerConfig() {
      this.threadType = SchedulerThreadType.SYNC;
   }

   public @Nullable Integer everyTicks() {
      return this.everyTicks;
   }

   public @NotNull SchedulerConfig everyTicks(@Nullable Integer everyTicks) {
      this.everyTicks = everyTicks;
      return this;
   }

   public @Nullable Integer afterTicks() {
      return this.afterTicks;
   }

   public @NotNull SchedulerConfig afterTicks(@Nullable Integer afterTicks) {
      this.afterTicks = afterTicks;
      return this;
   }

   public @NotNull SchedulerConfig async() {
      this.threadType = SchedulerThreadType.ASYNC;
      return this;
   }

   public @NotNull SchedulerConfig sync() {
      this.threadType = SchedulerThreadType.SYNC;
      return this;
   }

   public @NotNull SchedulerThreadType threadType() {
      return this.threadType;
   }

   public @NotNull SchedulerConfig threadType(@NotNull SchedulerThreadType threadType) {
      this.threadType = threadType;
      return this;
   }
}
