package dev.viper.utils.location;

import dev.viper.utils.library.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationUtils {
   public static boolean isBetween(int a, int b, int numberToCheck) {
      return isBetween(a, b, numberToCheck, true);
   }

   public static boolean isBetween(int a, int b, int numberToCheck, boolean inclusive) {
      if (inclusive) {
         return numberToCheck >= Math.min(a, b) && numberToCheck <= Math.max(a, b);
      } else {
         return numberToCheck > Math.min(a, b) && numberToCheck < Math.max(a, b);
      }
   }

   public static boolean locIsBetween(@NotNull Location a, @NotNull Location b, @NotNull Location locToCheck) {
      return isBetween(a.getBlockX(), b.getBlockX(), locToCheck.getBlockX()) && isBetween(a.getBlockY(), b.getBlockY(), locToCheck.getBlockY()) && isBetween(a.getBlockZ(), b.getBlockZ(), locToCheck.getBlockZ());
   }

   public static @NotNull List<Player> playersInRadius(@NotNull Location center, double radius) {
      List<Player> nearbyPlayers = new ArrayList();

      for(Player player : Utils.plugin().getServer().getOnlinePlayers()) {
         Location playerLocation = player.getLocation();
         if (Objects.equals(center.getWorld(), playerLocation.getWorld()) && center.distance(playerLocation) <= radius) {
            nearbyPlayers.add(player);
         }
      }

      return nearbyPlayers;
   }

   public static @NotNull List<Player> playersNearPlayer(@NotNull Player targetPlayer, double radius) {
      List<Player> nearbyPlayers = new ArrayList();
      Location targetLocation = targetPlayer.getLocation();

      for(Player player : Utils.plugin().getServer().getOnlinePlayers()) {
         if (!player.equals(targetPlayer)) {
            Location playerLocation = player.getLocation();
            if (Objects.equals(targetLocation.getWorld(), playerLocation.getWorld()) && targetLocation.distance(playerLocation) <= radius) {
               nearbyPlayers.add(player);
            }
         }
      }

      return nearbyPlayers;
   }

   public static double distance(@NotNull Location loc1, @NotNull Location loc2) {
      return loc1.distance(loc2);
   }

   public static @NotNull String serialize(@NotNull Location location) {
      String var10000 = ((World)Objects.requireNonNull(location.getWorld())).getName();
      return var10000 + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
   }

   public static @Nullable Location deserialize(@NotNull String serialized) {
      String[] parts = serialized.split(",");
      if (parts.length == 6) {
         World world = Bukkit.getWorld(parts[0]);
         double x = Double.parseDouble(parts[1]);
         double y = Double.parseDouble(parts[2]);
         double z = Double.parseDouble(parts[3]);
         float pitch = Float.parseFloat(parts[4]);
         float yaw = Float.parseFloat(parts[5]);
         return new Location(world, x, y, z, yaw, pitch);
      } else {
         return null;
      }
   }
}
