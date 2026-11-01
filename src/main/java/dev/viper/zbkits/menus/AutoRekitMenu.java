package dev.viper.zbkits.menus;

import dev.viper.utils.compat.Compat;
import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.utils.folia.FoliaUtils;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.menu.xmenu.ItemBuilderGUI;
import dev.viper.utils.text.color.ColorizeText;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AutoRekitMenu extends GuiBuilder {
   private static final zbKits PLUGIN = zbKits.getInstance();
   private static final MenuConfig CONFIG;
   private static final Logger LOGGER;

   public AutoRekitMenu() {
      super(CONFIG.getConfig().getInt("auto_rekit.size"));
   }

   public static GuiBuilder openAutoRekitMenu(Player player) {
      GuiBuilder inventory = new GuiBuilder(CONFIG.getConfig().getInt("auto_rekit.size", 27), CONFIG.getConfig().getString("auto_rekit.title", "Auto Rekit"));
      addFilterItems(inventory);
      addToggleAutoRekitItem(inventory, player);
      addKitItems(inventory, player);
      return inventory;
   }

   private static void addFilterItems(GuiBuilder inventory) {
      List<Integer> filterSlots = CONFIG.getConfig().getIntegerList("auto_rekit.filter.slots");
      ItemStack filter = createItem("auto_rekit.filter", Material.BLACK_STAINED_GLASS_PANE);

      for(int slot : filterSlots) {
         inventory.setItem(slot, filter);
      }

   }

   private static void addToggleAutoRekitItem(GuiBuilder inventory, Player player) {
      String configPath = "auto_rekit.toggle_auto_rekit";
      Material defaultMaterial = Material.NETHERITE_SWORD;
      int slot = CONFIG.getConfig().getInt(configPath + ".slot");
      ItemStack toggleAutoRekit = createItem(configPath, defaultMaterial);
      setToggleAutoRekitLore(toggleAutoRekit, player);
      if (FoliaUtils.isFolia()) {
         AsyncScheduler asyncScheduler = Bukkit.getAsyncScheduler();
         inventory.setItem(slot, toggleAutoRekit, (p) -> asyncScheduler.runNow(zbKits.getInstance(), (ignored) -> {
               zbKits.getAutoRekitUtil().toggle(player);
               setToggleAutoRekitLore(toggleAutoRekit, player);
               player.getOpenInventory().setItem(slot, toggleAutoRekit);
               player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1.0F, 1.0F);
            }));
      } else {
         Bukkit.getScheduler().runTaskAsynchronously(zbKits.getInstance(), () -> {
            zbKits.getAutoRekitUtil().toggle(player);
            setToggleAutoRekitLore(toggleAutoRekit, player);
            player.getOpenInventory().setItem(slot, toggleAutoRekit);
            player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1.0F, 1.0F);
         });
      }

   }

   private static void setToggleAutoRekitLore(ItemStack toggleAutoRekit, Player player) {
      String configPath = "auto_rekit.toggle_auto_rekit";
      List<String> lore = new ArrayList();

      for(String line : CONFIG.getConfig().getStringList(configPath + ".lore")) {
         lore.add(ColorizeText.hex(line.replace("%status%", zbKits.getAutoRekitUtil().status(player))));
      }

      ItemMeta meta = toggleAutoRekit.getItemMeta();
      meta.setLore(lore);
      meta.setDisplayName(ColorizeText.hex((String)Objects.requireNonNull(CONFIG.getConfig().getString(configPath + ".name"))));
      toggleAutoRekit.setItemMeta(meta);
   }

   private static void addKitItems(GuiBuilder inventory, Player player) {
      addItemGroup(inventory, player);
   }

   private static void addItemGroup(GuiBuilder inventory, Player player) {
      List<Integer> slots = CONFIG.getConfig().getIntegerList("auto_rekit.kits.slots");

      for(int i = 0; i < slots.size(); ++i) {
         int slot = (Integer)slots.get(i);
         int kitNumber = i + 1;
         ItemStack item = createItem("auto_rekit.kits", Material.END_CRYSTAL, kitNumber);
         inventory.setItem(slot, item, (p) -> {
            String kitSelected = PLUGIN.getConfig().getString("messages.auto_rekit_kit_selected");

            assert kitSelected != null;

            player.sendMessage(ColorizeText.hex(kitSelected).replace("%kit%", String.valueOf(kitNumber)));
            zbKits.getAutoRekitUtil().setKit(player, "Kit " + kitNumber);
         });
      }

   }

   private static ItemStack createItem(String configPath, Material defaultMaterial) {
      return createItem(configPath, defaultMaterial, 0);
   }

   private static ItemStack createItem(String configPath, Material defaultMaterial, int kitNumber) {
      String itemMaterial = CONFIG.getConfig().getString(configPath + ".material", defaultMaterial.name());
      String itemName = CONFIG.getConfig().getString(configPath + ".name", "").replace("%kit%", String.valueOf(kitNumber));
      List<String> loreList = CONFIG.getConfig().getStringList(configPath + ".lore");
      List<String> finalLore = new ArrayList();

      for(String lore : loreList) {
         finalLore.add(ColorizeText.hex(lore.replace("%i%", String.valueOf(kitNumber))));
      }

      List<String> flagList = CONFIG.getConfig().getStringList(configPath + ".flags");
      List<ItemFlag> flags = new ArrayList();

      for(String flag : flagList) {
         flags.add(ItemFlag.valueOf(flag));
      }

      ItemStack item = (new ItemBuilderGUI(Material.valueOf(itemMaterial))).name(ColorizeText.hex(itemName)).lore((String[])finalLore.toArray(new String[0])).flags((ItemFlag[])flags.toArray(new ItemFlag[0])).build();

      for(Map<?, ?> enchantmentMap : CONFIG.getConfig().getMapList(configPath + ".enchantments")) {
         String type = (String)enchantmentMap.get("type");
         int level = (Integer)enchantmentMap.get("level");
         Enchantment enchantment = Compat.enchantment(type);
         if (enchantment != null) {
            item.addUnsafeEnchantment(enchantment, level);
         } else {
            LOGGER.warning("Invalid enchantment type " + type);
         }
      }

      return item;
   }

   static {
      CONFIG = new MenuConfig(PLUGIN, "menus/autorekit_menu.yml");
      LOGGER = PLUGIN.getLogger();
   }
}
