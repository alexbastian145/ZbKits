package dev.viper.zbkits.menus;

import dev.viper.zbkits.utils.Materials;
import dev.viper.utils.compat.Compat;
import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.menu.xmenu.ItemBuilderGUI;
import dev.viper.utils.text.color.ColorizeText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitEditorMenu extends GuiBuilder {
   private static final zbKits PLUGIN = zbKits.getInstance();
   private static final MenuConfig CONFIG;
   private static final Logger LOGGER;

   public KitEditorMenu(int size) {
      super(size);
   }

   public static void openKitEditor(Player player, String kitName) {
      int inventorySize = CONFIG.getConfig().getInt("size", 54);
      String titleTemplate = CONFIG.getConfig().getString("title", "Editing %kitname%");
      String inventoryTitle = ColorizeText.hex(titleTemplate.replace("%kitname%", kitName));
      GuiBuilder inventory = new GuiBuilder(inventorySize, inventoryTitle);
      zbKits.getKitUtil().set(player, kitName, inventory);
      addFilter(inventory, inventorySize);
      addItems(inventory, player, kitName);
      inventory.addClickHandler((event) -> {
         int slot = event.getRawSlot();
         if (slot >= 0 && slot <= 40) {
            event.setCancelled(false);
         }

      });
      inventory.open(player);
   }

   private static void addFilter(GuiBuilder inventory, int inventorySize) {
      for(int i = 1; i <= 4; ++i) {
         int slot = i + (inventorySize - 14);
         ItemStack filter = (new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)).name(" ").flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
         inventory.setItem(slot, filter);
         inventory.setItem(inventorySize - 9, filter);
         inventory.setItem(inventorySize - 7, filter);
         inventory.setItem(inventorySize - 6, filter);
         inventory.setItem(inventorySize - 3, filter);
         inventory.setItem(inventorySize - 1, filter);
      }

   }

   private static void addItems(GuiBuilder inventory, Player player, String kitName) {
      addItem(inventory, "save", Material.LIME_DYE, inventory.getInventory().getSize() - 8, player, kitName);
      addItem(inventory, "reset", Material.RED_DYE, inventory.getInventory().getSize() - 6, player, kitName);
      addItem(inventory, "importInventory", Material.CHEST, inventory.getInventory().getSize() - 4, player, kitName);
      addItem(inventory, "premadeKit", Material.NETHERITE_CHESTPLATE, inventory.getInventory().getSize() - 2, player, kitName);
      addItem(inventory, "back", Material.RED_STAINED_GLASS_PANE, inventory.getInventory().getSize() - 5, player, kitName);
   }

   private static void addItem(GuiBuilder inventory, String configName, Material defaultMaterial, int defaultSlot, Player player, String kitName) {
      String itemMaterial = CONFIG.getConfig().getString("kit_editor." + configName + ".material", defaultMaterial.name());
      String itemName = CONFIG.getConfig().getString("kit_editor." + configName + ".name", "");
      int itemSlot = CONFIG.getConfig().getInt("kit_editor." + configName + ".slot", defaultSlot);
      List<String> loreList = CONFIG.getConfig().getStringList("kit_editor." + configName + ".lore");
      List<String> finalLore = new ArrayList();

      for(String lore : loreList) {
         finalLore.add(ColorizeText.hex(lore));
      }

      List<String> flagList = CONFIG.getConfig().getStringList("kit_editor." + configName + ".flags");
      List<ItemFlag> flags = new ArrayList();

      for(String flag : flagList) {
         try {
            flags.add(ItemFlag.valueOf(flag));
         } catch (IllegalArgumentException var20) {
            LOGGER.warning("Invalid item flag " + flag);
         }
      }

      List<Map<?, ?>> enchantmentList = CONFIG.getConfig().getMapList("kit_editor." + configName + ".enchantments");
      Map<Enchantment, Integer> enchantments = new HashMap();

      for(Map<?, ?> enchantmentMap : enchantmentList) {
         String type = (String)enchantmentMap.get("type");
         int level = (Integer)enchantmentMap.get("level");
         Enchantment enchantment = Compat.enchantment(type);
         if (enchantment != null) {
            enchantments.put(enchantment, level);
         } else {
            LOGGER.warning("Invalid enchantment type " + type);
         }
      }

      ItemStack item = (new ItemBuilderGUI(Materials.parse(itemMaterial))).name(ColorizeText.hex(itemName)).flags((ItemFlag[])flags.toArray(new ItemFlag[0])).lore((String[])finalLore.toArray(new String[0])).build();

      for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
         item.addUnsafeEnchantment((Enchantment)entry.getKey(), (Integer)entry.getValue());
      }

      inventory.setItem(itemSlot, item, (p) -> {
         switch (configName) {
            case "save":
               zbKits.getKitUtil().save(player, kitName);
               KitsMenu.openKitMenu(player).open(player);
               break;
            case "reset":
               for(int i = 0; i <= 40; ++i) {
                  inventory.setItem(i, new ItemStack(Material.AIR));
               }
               break;
            case "importInventory":
               zbKits.getKitUtil().importInventory(player, inventory);
               break;
            case "premadeKit":
               zbKits.getPremadeKitUtil().load(player);
               break;
            case "back":
               KitsMenu.openKitMenu(player).open(player);
         }

      });
   }

   static {
      CONFIG = new MenuConfig(PLUGIN, "menus/kiteditor_menu.yml");
      LOGGER = PLUGIN.getLogger();
   }
}
