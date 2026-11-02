package dev.viper.utils.commodore;

import java.util.Objects;
import java.util.function.Function;
import org.bukkit.plugin.Plugin;

public final class CommodoreProvider {
   private static final Function<Plugin, Commodore> PROVIDER = checkSupported();

   private CommodoreProvider() {
      throw new AssertionError();
   }

   private static Function<Plugin, Commodore> checkSupported() {
      try {
         Class.forName("com.mojang.brigadier.CommandDispatcher");
      } catch (Throwable e) {
         printDebugInfo(e);
         return null;
      }

      try {
         PaperCommodore.ensureSetup();
         return PaperCommodore::new;
      } catch (Throwable e) {
         printDebugInfo(e);

         try {
            ReflectionCommodore.ensureSetup();
            return ReflectionCommodore::new;
         } catch (Throwable ex) {
            printDebugInfo(ex);
            return null;
         }
      }
   }

   private static void printDebugInfo(Throwable e) {
      if (System.getProperty("commodore.debug") != null) {
         System.err.println("Exception while initialising commodore:");
         e.printStackTrace(System.err);
      }

   }

   public static boolean isSupported() {
      return PROVIDER != null;
   }

   public static Commodore getCommodore(Plugin plugin) throws BrigadierUnsupportedException {
      Objects.requireNonNull(plugin, "plugin");
      if (PROVIDER == null) {
         throw new BrigadierUnsupportedException("Brigadier is not supported by the server. Set -Dcommodore.debug=true for debug info.");
      } else {
         return (Commodore)PROVIDER.apply(plugin);
      }
   }
}
