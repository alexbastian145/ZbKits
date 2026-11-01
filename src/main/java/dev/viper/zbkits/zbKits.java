package dev.viper.zbkits;

import dev.viper.zbkits.commands.AutoRekitCommand;
import dev.viper.zbkits.commands.KitCommand;
import dev.viper.zbkits.commands.KitLoadCommand;
import dev.viper.zbkits.commands.KitRoomAdminCommand;
import dev.viper.zbkits.commands.PremadeKitCommand;
import dev.viper.zbkits.commands.admin.KitsAdminCommand;
import dev.viper.zbkits.hooks.HooksImpl;
import dev.viper.zbkits.hooks.Metrics;
import dev.viper.zbkits.listeners.AutoRekitAnchorListener;
import dev.viper.zbkits.listeners.AutoRekitListener;
import dev.viper.zbkits.utils.AutoRekitUtil;
import dev.viper.zbkits.utils.EnderChestUtil;
import dev.viper.zbkits.utils.KitRoomUtil;
import dev.viper.zbkits.utils.KitUtil;
import dev.viper.zbkits.utils.PremadeKitUtil;
import dev.viper.zbkits.utils.config.ConfigManager;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.utils.library.Utils;
import dev.viper.utils.library.wrapper.PluginWrapper;
import dev.viper.utils.menu.xmenu.GuiManager;
import dev.viper.utils.misc.Versions;
import dev.viper.utils.server.Servers;
import dev.viper.utils.text.color.ColorizeText;
import org.bukkit.command.ConsoleCommandSender;

public final class zbKits extends PluginWrapper {
   private static zbKits instance;
   private static KitUtil kitUtil;
   private static PremadeKitUtil premadeKitUtil;
   private static EnderChestUtil enderChestUtil;
   private static KitRoomUtil kitRoomUtil;
   private static AutoRekitUtil autoRekitUtil;

   protected void start() {
      instance = this;
      Utils.init(this, false, "zbKits");
      GuiManager.register(this);
      this.saveDefaultConfig();
      ConsoleCommandSender log = Servers.server().getConsoleSender();
      ConfigManager configManager = ConfigManager.get(this);
      MenuConfig.of(this);
      KitUtil.of(this);
      kitUtil = new KitUtil(configManager);
      PremadeKitUtil.of(this);
      premadeKitUtil = new PremadeKitUtil(configManager);
      EnderChestUtil.of(this);
      enderChestUtil = new EnderChestUtil(configManager);
      KitRoomUtil.of(this);
      kitRoomUtil = new KitRoomUtil(configManager);
      AutoRekitUtil.of(this);
      autoRekitUtil = new AutoRekitUtil(configManager);
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
      int kits = this.getConfig().getInt("kits");

      for(int i = 1; i <= kits; ++i) {
         new KitLoadCommand(this, "kit" + i, i);
      }

      new Metrics(this, 22161);
      HooksImpl.of(this);
      log.sendMessage(ColorizeText.hex("&c&lzbKits &7› &fThe &#ff2e2eplugin &fhas been successfully enabled, created by &#ff2e2ezarbunex &fwith &#ff2e2e❤"));
      log.sendMessage(ColorizeText.hex("&c&lzbKits &7› &fJoin our &#ff2e2esupport server &fat &#ff2e2ehttps://xyris.fun/devs/"));
   }

   protected void stop() {
      getKitUtil().saveAll();
      getEnderChestUtil().saveAll();
      getPremadeKitUtil().saveAll();
   }

   public static zbKits getInstance() {
      return instance;
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
