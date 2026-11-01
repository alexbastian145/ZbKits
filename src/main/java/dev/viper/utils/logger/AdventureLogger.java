package dev.viper.utils.logger;

import dev.viper.utils.library.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class AdventureLogger {
   private static final ComponentLogger LOGGER = Utils.plugin().getComponentLogger();

   public void debug(Component component) {
      LOGGER.debug(component);
   }

   public void warn(Component component) {
      LOGGER.warn(component);
   }

   public void error(Component component) {
      LOGGER.error(component);
   }

   public void info(Component component) {
      LOGGER.info(component);
   }

   public void trace(Component component) {
      LOGGER.trace(component);
   }
}
