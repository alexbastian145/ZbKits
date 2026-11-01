package dev.viper.utils.logger;

import dev.viper.utils.library.Utils;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public class Loggers {
   public static AdventureLogger adventure() {
      return new AdventureLogger();
   }

   public static AdventureLogger component() {
      return new AdventureLogger();
   }

   public static Logger prefixed() {
      return Utils.plugin().getLogger();
   }

   public static Logger prefixed(JavaPlugin plugin) {
      return plugin.getLogger();
   }

   public static Logger prefixed(Plugin plugin) {
      return plugin.getLogger();
   }

   public static Logger server() {
      return Utils.plugin().getServer().getLogger();
   }

   public static Logger server(JavaPlugin plugin) {
      return plugin.getServer().getLogger();
   }

   public static Logger server(Plugin plugin) {
      return plugin.getServer().getLogger();
   }

   public static <L> org.slf4j.Logger logger(@NotNull Class<L> clazz) {
      return LoggerFactory.getLogger(clazz);
   }
}
