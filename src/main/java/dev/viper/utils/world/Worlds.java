package dev.viper.utils.world;

import dev.viper.utils.library.Utils;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Worlds {
   public static @NotNull List<World> worlds() {
      return Utils.plugin().getServer().getWorlds();
   }

   public static @Nullable World world(@NotNull String name) {
      return world(Utils.plugin(), name);
   }

   public static @Nullable World world(@NotNull UUID uuid) {
      return world(Utils.plugin(), uuid);
   }

   public static @Nullable World world(@NotNull JavaPlugin plugin, @NotNull String name) {
      return plugin.getServer().getWorld(name);
   }

   public static @Nullable World world(@NotNull JavaPlugin plugin, @NotNull UUID uuid) {
      return plugin.getServer().getWorld(uuid);
   }

   public static @NotNull String name(@NotNull World world) {
      return world.getName();
   }

   public static @NotNull String name(@NotNull JavaPlugin plugin, @NotNull UUID uuid) {
      return ((World)Objects.requireNonNull(plugin.getServer().getWorld(uuid))).getName();
   }

   public static boolean worldExists(@NotNull JavaPlugin plugin, @NotNull String name) {
      return plugin.getServer().getWorld(name) != null;
   }

   public static @Nullable World create(@NotNull JavaPlugin plugin, @NotNull WorldCreator properties) {
      return plugin.getServer().createWorld(properties);
   }

   public static World create(WorldCreator properties) {
      return Utils.plugin().getServer().createWorld(properties);
   }

   public static boolean isSameWorld(@NotNull Player playerOne, @NotNull Player playerTwo) {
      return isSameWorld(playerOne.getWorld(), playerTwo.getWorld());
   }

   public static boolean isSameWorld(@NotNull World worldOne, @NotNull World worldTwo) {
      return worldOne.getName().equalsIgnoreCase(worldTwo.getName());
   }

   public static boolean isSameWorld(@NotNull String worldOne, @NotNull String worldTwo) {
      return worldOne.equalsIgnoreCase(worldTwo);
   }
}
