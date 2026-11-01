package dev.viper.utils.folia;

public class FoliaUtils {
   public static boolean hasFolia() {
      try {
         Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }

   public static boolean isFolia() {
      return hasFolia();
   }
}
