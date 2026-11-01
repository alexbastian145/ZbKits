package dev.viper.zbkits.api;

import dev.viper.zbkits.utils.AutoRekitUtil;
import dev.viper.zbkits.utils.EnderChestUtil;
import dev.viper.zbkits.utils.KitRoomUtil;
import dev.viper.zbkits.utils.KitUtil;
import dev.viper.zbkits.utils.PremadeKitUtil;
import dev.viper.zbkits.utils.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsApiProvider {
   private static KitsAPI kitsAPI;
   private static PremadeKitAPI premadeKitAPI;
   private static KitRoomAPI kitRoomAPI;
   private static EnderChestAPI enderChestAPI;
   private static AutoRekitAPI autoRekitAPI;

   public static void init(JavaPlugin plugin) {
      ConfigManager configManager = ConfigManager.get(plugin);
      kitsAPI = new KitUtil(configManager);
      premadeKitAPI = new PremadeKitUtil(configManager);
      kitRoomAPI = new KitRoomUtil(configManager);
      enderChestAPI = new EnderChestUtil(configManager);
      autoRekitAPI = new AutoRekitUtil(configManager);
   }

   public static KitsApiProvider get() {
      return new KitsApiProvider();
   }

   public KitsAPI getKitsAPI() {
      return kitsAPI;
   }

   public PremadeKitAPI getPremadeKitAPI() {
      return premadeKitAPI;
   }

   public KitRoomAPI getKitRoomAPI() {
      return kitRoomAPI;
   }

   public EnderChestAPI getEnderChestAPI() {
      return enderChestAPI;
   }

   public AutoRekitAPI getAutoRekitAPI() {
      return autoRekitAPI;
   }
}
