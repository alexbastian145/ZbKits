package dev.viper.utils.library;

import dev.viper.utils.commodore.Commodore;
import dev.viper.utils.commodore.CommodoreProvider;
import dev.viper.utils.event.crystal.impl.SpigotAnchorEventListener;
import dev.viper.utils.event.crystal.impl.SpigotCrystalEventListener;
import dev.viper.utils.misc.Versions;
import dev.viper.utils.registration.Registrar;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {
   private static JavaPlugin plugin;
   private static Commodore commodore;

   private Utils(@NotNull JavaPlugin plugin) {
      Utils.plugin = plugin;
      if (Versions.isHigherThanOrEqualTo("1.20.1")) {
         try {
            Registrar.events(new SpigotAnchorEventListener());
         } catch (Exception var3) {
         }
      }

      if (CommodoreProvider.isSupported()) {
         commodore = CommodoreProvider.getCommodore(plugin);
      }

      Registrar.events(new SpigotCrystalEventListener());
   }

   public static @NotNull Utils init(@NotNull JavaPlugin plugin) {
      return new Utils(plugin);
   }

   public static @NotNull JavaPlugin plugin() {
      return plugin;
   }

   public static @Nullable Commodore commodore() {
      return commodore;
   }

   public void plugin(@NotNull JavaPlugin plugin) {
      Utils.plugin = plugin;
   }
}
