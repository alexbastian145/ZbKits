package dev.viper.utils.update;

import dev.viper.utils.library.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Updater {
   public static String latestVersion(@NotNull JavaPlugin plugin, int resourceId) {
      try {
         InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)).openStream();

         String var4;
         label70: {
            try {
               Scanner scanner = new Scanner(inputStream);

               label54: {
                  try {
                     if (!scanner.hasNext()) {
                        break label54;
                     }

                     var4 = scanner.next();
                  } catch (Throwable var8) {
                     try {
                        scanner.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }

                     throw var8;
                  }

                  scanner.close();
                  break label70;
               }

               scanner.close();
            } catch (Throwable var9) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var6) {
                     var9.addSuppressed(var6);
                  }
               }

               throw var9;
            }

            if (inputStream != null) {
               inputStream.close();
            }

            return null;
         }

         if (inputStream != null) {
            inputStream.close();
         }

         return var4;
      } catch (IOException e) {
         plugin.getLogger().severe("Failed to fetch latest version: " + e.getMessage());
         e.printStackTrace();
         return null;
      }
   }

   public static boolean isOutdated(@NotNull JavaPlugin plugin, int resourceId) {
      String latestVersion = latestVersion(plugin, resourceId);
      if (latestVersion != null) {
         String currentVersion = plugin.getPluginMeta().getVersion();
         return !currentVersion.equalsIgnoreCase(latestVersion);
      } else {
         return false;
      }
   }

   public static void scheduledUpdateChecker(@NotNull JavaPlugin plugin, int resourceId, long every, @NotNull UpdateCallback callback) {
      plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
         if (isOutdated(plugin, resourceId)) {
            callback.onUpdate();
         }

      }, 0L, every);
   }

   public static String latestVersion(int resourceId) {
      JavaPlugin plugin = Utils.plugin();

      try {
         InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)).openStream();

         String var4;
         label74: {
            try {
               Scanner scanner = new Scanner(inputStream);

               label56: {
                  try {
                     if (!scanner.hasNext()) {
                        break label56;
                     }

                     var4 = scanner.next();
                  } catch (Throwable var8) {
                     try {
                        scanner.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }

                     throw var8;
                  }

                  scanner.close();
                  break label74;
               }

               scanner.close();
            } catch (Throwable var9) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var6) {
                     var9.addSuppressed(var6);
                  }
               }

               throw var9;
            }

            if (inputStream != null) {
               inputStream.close();
            }

            return null;
         }

         if (inputStream != null) {
            inputStream.close();
         }

         return var4;
      } catch (IOException e) {
         plugin.getLogger().severe("Failed to fetch latest version: " + e.getMessage());
         e.printStackTrace();
         return null;
      }
   }

   public static boolean isOutdated(int resourceId) {
      JavaPlugin plugin = Utils.plugin();
      String latestVersion = latestVersion(resourceId);
      if (latestVersion != null) {
         String currentVersion = plugin.getPluginMeta().getVersion();
         return !currentVersion.equalsIgnoreCase(latestVersion);
      } else {
         return false;
      }
   }

   public static void scheduledUpdateChecker(int resourceId, long every, @NotNull UpdateCallback callback) {
      JavaPlugin plugin = Utils.plugin();
      plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
         if (isOutdated(resourceId)) {
            callback.onUpdate();
         }

      }, 0L, every);
   }

   public interface UpdateCallback {
      void onUpdate();
   }
}
