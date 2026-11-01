package dev.viper.zbkits.menus;

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

public class EnderChestEditor extends GuiBuilder {
   private static final zbKits PLUGIN = zbKits.getInstance();
   private static final MenuConfig CONFIG;
   private static final Logger LOGGER;

   public EnderChestEditor(int size) {
      super(size);
   }

   public static void openEnderChestEditor(Player player, String kitName) {
      int inventorySize = CONFIG.getConfig().getInt("enderchest_editor.size", 36);
      String titleTemplate = CONFIG.getConfig().getString("enderchest_editor.title", "Ender Chest | %kitname%");
      String inventoryTitle = ColorizeText.hex(titleTemplate.replace("%kitname%", kitName));
      GuiBuilder inventory = new GuiBuilder(inventorySize, inventoryTitle);
      zbKits.getEnderChestUtil().set(player, kitName, inventory);
      addFilterItems(inventory);
      addItems(inventory, player, kitName);
      inventory.addClickHandler((event) -> {
         int slot = event.getRawSlot();
         if (slot >= 0 && slot <= 26) {
            event.setCancelled(false);
         }

      });
      inventory.open(player);
   }

   private static void addFilterItems(GuiBuilder inventory) {
      String filterMaterial = CONFIG.getConfig().getString("enderchest_editor.filter.material", "BLACK_STAINED_GLASS_PANE");
      String filterName = CONFIG.getConfig().getString("enderchest_editor.filter.name", " ");
      List<String> filterFlagsList = CONFIG.getConfig().getStringList("enderchest_editor.filter.flags");
      List<ItemFlag> filterFlags = new ArrayList();

      for(String flag : filterFlagsList) {
         filterFlags.add(ItemFlag.valueOf(flag));
      }

      List<Integer> filterSlots = CONFIG.getConfig().getIntegerList("enderchest_editor.filter.slots");
      ItemStack filter = (new ItemBuilderGUI(Material.valueOf(filterMaterial))).name(filterName).flags((ItemFlag[])filterFlags.toArray(new ItemFlag[0])).build();

      for(int slot : filterSlots) {
         inventory.setItem(slot, filter);
      }

   }

   private static void addItems(GuiBuilder inventory, Player player, String kitName) {
      addItem(inventory, "save", Material.LIME_DYE, player, kitName);
      addItem(inventory, "reset", Material.RED_DYE, player, kitName);
      addItem(inventory, "back", Material.RED_STAINED_GLASS_PANE, player, kitName);
   }

   private static void addItem(GuiBuilder inventory, String configName, Material defaultMaterial, Player player, String kitName) {
      String itemMaterial = CONFIG.getConfig().getString("enderchest_editor." + configName + ".material", defaultMaterial.name());
      String itemName = CONFIG.getConfig().getString("enderchest_editor." + configName + ".name", "");
      int itemSlot = CONFIG.getConfig().getInt("enderchest_editor." + configName + ".slot");
      List<String> loreList = CONFIG.getConfig().getStringList("enderchest_editor." + configName + ".lore");
      List<String> finalLore = new ArrayList();

      for(String lore : loreList) {
         finalLore.add(ColorizeText.hex(lore));
      }

      List<String> flagList = CONFIG.getConfig().getStringList("enderchest_editor." + configName + ".flags");
      List<ItemFlag> flags = new ArrayList();

      for(String flag : flagList) {
         try {
            flags.add(ItemFlag.valueOf(flag));
         } catch (IllegalArgumentException var19) {
            LOGGER.warning("Invalid item flag " + flag);
         }
      }

      List<Map<?, ?>> enchantmentList = CONFIG.getConfig().getMapList("enderchest_editor." + configName + ".enchantments");
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

      ItemStack item = (new ItemBuilderGUI(Material.valueOf(itemMaterial))).name(ColorizeText.hex(itemName)).flags((ItemFlag[])flags.toArray(new ItemFlag[0])).lore((String[])finalLore.toArray(new String[0])).build();

      for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
         item.addUnsafeEnchantment((Enchantment)entry.getKey(), (Integer)entry.getValue());
      }

      inventory.setItem(itemSlot, item, (p) -> {
         switch (configName) {
            case "save":
               zbKits.getEnderChestUtil().save(player, kitName);
               KitsMenu.openKitMenu(player).open(player);
               break;
            case "reset":
               for(int i = 0; i <= 26; ++i) {
                  inventory.setItem(i, new ItemStack(Material.AIR));
               }
               break;
            case "back":
               KitsMenu.openKitMenu(player).open(player);
         }

      });
   }

   static {
      CONFIG = new MenuConfig(PLUGIN, "menus/enderchest_editor_menu.yml");
      LOGGER = PLUGIN.getLogger();
   }
}
