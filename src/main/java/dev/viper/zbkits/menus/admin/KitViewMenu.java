package dev.viper.zbkits.menus.admin;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.menu.xmenu.ItemBuilderGUI;
import dev.viper.utils.text.color.ColorizeText;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitViewMenu extends GuiBuilder {
   private static final zbKits PLUGIN = zbKits.getInstance();
   private static final MenuConfig CONFIG;

   public KitViewMenu() {
      super(36);
   }

   public static void openKitSelectMenu(Player executor, String targetPlayerName) {
      GuiBuilder inventory = new GuiBuilder(36, targetPlayerName + " - Kits");
      List<Integer> slots = CONFIG.getConfig().getIntegerList("kits_menu.kits.slots");
      List<Integer> slots1 = CONFIG.getConfig().getIntegerList("kits_menu.enderchests.slots");

      for(int s = 0; s < inventory.getInventory().getSize(); ++s) {
         ItemStack filter = (new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)).name(" ").flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
         inventory.setItem(s, filter);
      }

      for(int i = 0; i < slots.size(); ++i) {
         int slot = (Integer)slots.get(i);
         int kitNumber = i + 1;
         ItemStack back = (new ItemBuilderGUI(Material.END_CRYSTAL)).name(ColorizeText.mm("<#ff2e2e>Kit " + kitNumber)).flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
         inventory.setItem(slot, back, (p) -> kit(executor, targetPlayerName, "Kit " + kitNumber));
      }

      for(int i = 0; i < slots1.size(); ++i) {
         int slot = (Integer)slots1.get(i);
         int kitNumber = i + 1;
         ItemStack enderchest = (new ItemBuilderGUI(Material.ENDER_CHEST)).name(ColorizeText.mm("<#4561a3>Ender Chest " + kitNumber)).flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
         inventory.setItem(slot, enderchest, (p) -> ec(executor, targetPlayerName, "Kit " + kitNumber));
      }

      inventory.open(executor);
   }

   public static void kit(Player executor, String targetPlayerName, String kitName) {
      OfflinePlayer target = Bukkit.getOfflinePlayer(targetPlayerName);
      GuiBuilder inventory = new GuiBuilder(45, targetPlayerName + " - " + kitName);
      zbKits.getKitUtil().set(target.getUniqueId(), kitName, inventory);

      for(int i = 1; i <= 3; ++i) {
         int slot = i + 40;
         ItemStack filter = (new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)).name(" ").flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
         inventory.setItem(slot, filter);
      }

      ItemStack back = (new ItemBuilderGUI(Material.RED_STAINED_GLASS_PANE)).name(ColorizeText.mm("<#ffa6a6>Back")).flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
      inventory.setItem(44, back, (p) -> openKitSelectMenu(executor, targetPlayerName));
      inventory.open(executor);
   }

   public static void ec(Player executor, String targetPlayerName, String kitName) {
      OfflinePlayer target = Bukkit.getOfflinePlayer(targetPlayerName);
      GuiBuilder inventory = new GuiBuilder(36, targetPlayerName + " - Ender Chest" + kitName);
      zbKits.getEnderChestUtil().set(target.getUniqueId(), kitName, inventory);

      for(int i = 1; i <= 8; ++i) {
         int slot = i + 26;
         ItemStack filter = (new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)).name(" ").flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
         inventory.setItem(slot, filter);
      }

      ItemStack back = (new ItemBuilderGUI(Material.RED_STAINED_GLASS_PANE)).name(ColorizeText.mm("<#ffa6a6>Back")).flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
      inventory.setItem(35, back, (p) -> openKitSelectMenu(executor, targetPlayerName));
      inventory.open(executor);
   }

   static {
      CONFIG = new MenuConfig(PLUGIN, "menus/kits_menu.yml");
   }
}
