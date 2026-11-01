package dev.viper.utils.scheduler.stacker;

import dev.viper.utils.scheduler.builder.SchedulerBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SchedulerStacker {
   private final @NotNull List<SchedulerBuilder> builders = new ArrayList();

   public @NotNull SchedulerStacker stack(@NotNull SchedulerBuilder builder) {
      this.builders.add(builder);
      return this;
   }

   public @NotNull SchedulerStacker stack(@NotNull List<SchedulerBuilder> builders) {
      this.builders.addAll(builders);
      return this;
   }

   public @NotNull SchedulerStacker stack(SchedulerBuilder... builders) {
      this.builders.addAll(Arrays.asList(builders));
      return this;
   }

   public void execute() {
      for(SchedulerBuilder builder : this.builders) {
         builder.execute();
      }

   }
}
