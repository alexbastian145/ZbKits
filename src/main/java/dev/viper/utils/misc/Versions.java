package dev.viper.utils.misc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class Versions {
   public static boolean isVersion(@NotNull String version) {
      return version().equalsIgnoreCase(version);
   }

   public static boolean isHigherThanOrEqualTo(@NotNull String version) {
      return isVersion(version) || compareVersions(version(), version) > 0;
   }

   public static boolean isLowerThan(@NotNull String version) {
      return !isHigherThanOrEqualTo(version);
   }

   private static int compareVersions(@NotNull String versionOne, @NotNull String versionTwo) {
      List<Integer> v1Parts = getVersionParts(versionOne);
      List<Integer> v2Parts = getVersionParts(versionTwo);
      int minLength = Math.min(v1Parts.size(), v2Parts.size());

      for(int i = 0; i < minLength; ++i) {
         int partComparison = Integer.compare((Integer)v1Parts.get(i), (Integer)v2Parts.get(i));
         if (partComparison != 0) {
            return partComparison;
         }
      }

      return Integer.compare(v1Parts.size(), v2Parts.size());
   }

   private static @NotNull List<Integer> getVersionParts(@NotNull String version) {
      int suffix = 0;

      while(suffix < version.length() && (Character.isDigit(version.charAt(suffix)) || version.charAt(suffix) == '.')) {
         ++suffix;
      }

      return (List)Stream.of(version.substring(0, suffix).split("\\.")).filter((part) -> !part.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
   }

   public static @NotNull String version() {
      return Bukkit.getMinecraftVersion();
   }
}
