package dev.viper.utils.server;

import dev.viper.utils.library.Utils;
import dev.viper.utils.library.wrapper.PluginWrapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;

public class Servers {
   public static @NotNull PluginManager handler() {
      return Utils.plugin().getServer().getPluginManager();
   }

   public static @NotNull StructureManager structures() {
      return Utils.plugin().getServer().getStructureManager();
   }

   public static @NotNull ScoreboardManager scoreboards() {
      return Utils.plugin().getServer().getScoreboardManager();
   }

   public static @NotNull ServicesManager services() {
      return Utils.plugin().getServer().getServicesManager();
   }

   public static @NotNull List<Player> online() {
      return new ArrayList(Utils.plugin().getServer().getOnlinePlayers());
   }

   public static @NotNull List<String> onlineNames() {
      List<String> list = new ArrayList();

      for(Player player : online()) {
         String name = player.getName();
         list.add(name);
      }

      return list;
   }

   public static int playerCount() {
      return online().size();
   }

   public static Plugin plugin(@NotNull String name) {
      return Utils.plugin().getServer().getPluginManager().getPlugin(name);
   }

   public static Server server(@NotNull String name) {
      return plugin(name).getServer();
   }

   public static @NotNull Server server() {
      return Utils.plugin().getServer();
   }

   public static @NotNull File dataFolder() {
      return Utils.plugin().getDataFolder();
   }

   public static @NotNull File dataFolder(@NotNull JavaPlugin plugin) {
      return plugin.getDataFolder();
   }

   public static @NotNull File dataFolder(@NotNull Plugin plugin) {
      return plugin.getDataFolder();
   }

   public static @NotNull FileConfiguration config() {
      return Utils.plugin().getConfig();
   }

   public static <P extends JavaPlugin> P plugin(@NotNull Class<P> clazz) {
      return (P)JavaPlugin.getPlugin(clazz);
   }

   public static <P extends PluginWrapper> P wrapped(@NotNull Class<P> clazz) {
      return (P)(plugin(clazz));
   }
}
