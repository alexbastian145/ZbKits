package dev.viper.utils.registration;

import dev.viper.utils.library.Utils;
import java.util.Objects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Registrar {
   public static void events(@NotNull JavaPlugin plugin, @NotNull Listener listener) {
      plugin.getServer().getPluginManager().registerEvents(listener, plugin);
   }

   public static void command(@NotNull JavaPlugin plugin, @NotNull String commandName, @NotNull CommandExecutor executor) {
      Objects.requireNonNull(plugin, "Plugin cannot be null");
      Objects.requireNonNull(executor, "CommandExecutor cannot be null");
      ((PluginCommand)Objects.requireNonNull(plugin.getCommand(commandName))).setExecutor(executor);
   }

   public static @NotNull CommandMap commandMap(@NotNull JavaPlugin plugin) {
      return plugin.getServer().getCommandMap();
   }

   public static void commandMap(@NotNull JavaPlugin plugin, @NotNull String commandName, @NotNull Command command) {
      ((CommandMap)Objects.requireNonNull(commandMap(plugin))).register(plugin.getName(), command);
   }

   public static void events(@NotNull Listener listener) {
      events(Utils.plugin(), listener);
   }

   public static void command(@NotNull String commandName, @NotNull CommandExecutor executor) {
      command(Utils.plugin(), commandName, executor);
   }

   public static @NotNull CommandMap commandMap() {
      return commandMap(Utils.plugin());
   }

   public static void commandMap(@NotNull String commandName, @NotNull Command command) {
      commandMap(Utils.plugin(), commandName, command);
   }
}
