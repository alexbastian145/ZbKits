package dev.viper.zbkits;

import dev.viper.zbkits.commands.AutoRekitCommand;
import dev.viper.zbkits.commands.KitCommand;
import dev.viper.zbkits.commands.KitLoadCommand;
import dev.viper.zbkits.commands.KitRoomAdminCommand;
import dev.viper.zbkits.commands.PremadeKitCommand;
import dev.viper.zbkits.commands.ZbKitsCommand;
import dev.viper.zbkits.commands.admin.KitsAdminCommand;
import dev.viper.zbkits.hooks.HooksImpl;
import dev.viper.zbkits.hooks.Metrics;
import dev.viper.zbkits.listeners.AutoRekitAnchorListener;
import dev.viper.zbkits.listeners.AutoRekitListener;
import dev.viper.zbkits.listeners.PlayerDataListener;
import dev.viper.zbkits.utils.AutoRekitUtil;
import dev.viper.zbkits.utils.EnderChestUtil;
import dev.viper.zbkits.utils.KitRoomUtil;
import dev.viper.zbkits.utils.KitUtil;
import dev.viper.zbkits.utils.PremadeKitUtil;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.zbkits.utils.data.LegacyDataMigrator;
import dev.viper.zbkits.utils.data.PlayerDataManager;
import dev.viper.utils.library.Utils;
import dev.viper.utils.library.wrapper.PluginWrapper;
import dev.viper.utils.menu.xmenu.GuiManager;
import dev.viper.utils.misc.Versions;
import dev.viper.utils.server.Servers;
import dev.viper.utils.text.color.ColorizeText;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public final class zbKits extends PluginWrapper {
   private static zbKits instance;
   private static KitUtil kitUtil;
   private static PremadeKitUtil premadeKitUtil;
   private static EnderChestUtil enderChestUtil;
   private static KitRoomUtil kitRoomUtil;
   private static AutoRekitUtil autoRekitUtil;
   private static PlayerDataManager playerData;
   private final List<String> registeredKitPermissions = new ArrayList<>();

   protected void start() {
      instance = this;
      Utils.init(this, false, "zbKits");
      GuiManager.register(this);
      this.saveDefaultConfig();
      ConsoleCommandSender log = Servers.server().getConsoleSender();
      ConfigManager configManager = ConfigManager.get(this);
      MenuConfig.of(this);
      playerData = new PlayerDataManager(this);
      LegacyDataMigrator.migrate(this, playerData);
      kitUtil = new KitUtil(playerData);
      PremadeKitUtil.of(this);
      premadeKitUtil = new PremadeKitUtil(configManager);
      enderChestUtil = new EnderChestUtil(playerData);
      KitRoomUtil.of(this);
      kitRoomUtil = new KitRoomUtil(configManager);
      autoRekitUtil = new AutoRekitUtil(playerData);
      registerKitPermissions();
      Servers.server().getPluginManager().registerEvents(new PlayerDataListener(), this);
      Servers.server().getPluginManager().registerEvents(new AutoRekitListener(), this);
      if (Versions.isHigherThanOrEqualTo("1.20.1")) {
         AutoRekitAnchorListener autoRekitAnchorListener = new AutoRekitAnchorListener();
         autoRekitAnchorListener.register(this);
      }

      new KitCommand(this);
      new KitRoomAdminCommand(this);
      new PremadeKitCommand(this);
      new AutoRekitCommand(this);
      new KitsAdminCommand(this);
      new ZbKitsCommand(this);
      int kits = this.getConfig().getInt("kits");

      for(int i = 1; i <= kits; ++i) {
         new KitLoadCommand(this, "kit" + i, i);
      }

      new Metrics(this, 22161);
      HooksImpl.of(this);
      log.sendMessage(ColorizeText.hex("&c&lzbKits &7› &fThe &#ff2e2eplugin &fhas been successfully enabled, created by &#ff2e2ezarbunex &fwith &#ff2e2e❤"));
      log.sendMessage(ColorizeText.hex("&c&lzbKits &7› &fJoin our &#ff2e2esupport server &fat &#ff2e2ehttps://discord.gg/QRj5EaWyC"));
   }

   protected void stop() {
      getPremadeKitUtil().saveAll();
      playerData.shutdown();
      unregisterKitPermissions();
   }

   /**
    * Registers zbkits.kit1 ... zbkits.kitN (N = "kits" in config.yml) with default true, so every
    * player may use every kit until a permission plugin takes a kit away.
    */
   private void registerKitPermissions() {
      PluginManager pluginManager = Servers.server().getPluginManager();
      int kits = this.getConfig().getInt("kits", 7);

      for(int i = 1; i <= kits; ++i) {
         String node = "zbkits.kit" + i;
         if (pluginManager.getPermission(node) == null) {
            pluginManager.addPermission(new Permission(node, "Allows loading Kit " + i, PermissionDefault.TRUE));
            registeredKitPermissions.add(node);
         }
      }

   }

   private void unregisterKitPermissions() {
      PluginManager pluginManager = Servers.server().getPluginManager();

      for(String node : registeredKitPermissions) {
         pluginManager.removePermission(node);
      }

      registeredKitPermissions.clear();
   }

   public static zbKits getInstance() {
      return instance;
   }

   public static PlayerDataManager getPlayerData() {
      return playerData;
   }

   public static KitUtil getKitUtil() {
      return kitUtil;
   }

   public static PremadeKitUtil getPremadeKitUtil() {
      return premadeKitUtil;
   }

   public static EnderChestUtil getEnderChestUtil() {
      return enderChestUtil;
   }

   public static KitRoomUtil getKitRoomUtil() {
      return kitRoomUtil;
   }

   public static AutoRekitUtil getAutoRekitUtil() {
      return autoRekitUtil;
   }
}
