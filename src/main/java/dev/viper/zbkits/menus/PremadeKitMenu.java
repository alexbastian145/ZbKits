package dev.viper.zbkits.menus;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.menu.xmenu.ItemBuilderGUI;
import dev.viper.utils.text.color.ColorizeText;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class PremadeKitMenu extends GuiBuilder {
   private static final zbKits PLUGIN = zbKits.getInstance();
   private static final MenuConfig CONFIG;

   public PremadeKitMenu() {
      super(CONFIG.getConfig().getInt("premade_kit.size"));
   }

   public static GuiBuilder createGui(Player player) {
      GuiBuilder inventory = new GuiBuilder(CONFIG.getConfig().getInt("premade_kit.size"), ColorizeText.hex((String)Objects.requireNonNull(CONFIG.getConfig().getString("premade_kit.title"))));
      zbKits.getInstance();
      zbKits.getPremadeKitUtil().set(inventory);
      addItems(inventory);
      addItem(inventory, player);
      return inventory;
   }

   private static void addItems(GuiBuilder inventory) {
      ConfigurationSection section = CONFIG.getConfig().getConfigurationSection("premade_kit.filter");
      if (section != null) {
         Material material = Material.matchMaterial((String)Objects.requireNonNull(section.getString("material")));
         String name = ColorizeText.hex(section.getString("name", ""));
         List<Integer> slots = section.getIntegerList("slots");
         List<String> flagList = CONFIG.getConfig().getStringList("premade_kit.filter.flags");
         List<ItemFlag> flags = new ArrayList();

         for(String flag : flagList) {
            flags.add(ItemFlag.valueOf(flag));
         }

         ItemStack item = (new ItemBuilderGUI(material)).name(name).flags((ItemFlag[])flags.toArray(new ItemFlag[0])).build();

         for(int slot : slots) {
            inventory.setItem(slot, item);
         }
      }

   }

   private static void addItem(GuiBuilder inventory, Player player) {
      ConfigurationSection section = CONFIG.getConfig().getConfigurationSection("premade_kit.back");
      if (section != null) {
         Material material = Material.matchMaterial((String)Objects.requireNonNull(section.getString("material")));
         String name = ColorizeText.hex(section.getString("name", ""));
         int slot = section.getInt("slot");
         List<String> flagList = CONFIG.getConfig().getStringList("premade_kit.back.flags");
         List<ItemFlag> flags = new ArrayList();

         for(String flag : flagList) {
            flags.add(ItemFlag.valueOf(flag));
         }

         ItemStack item = (new ItemBuilderGUI(material)).name(name).flags((ItemFlag[])flags.toArray(new ItemFlag[0])).build();
         inventory.setItem(slot, item, (p) -> KitsMenu.openKitMenu(player).open(player));
      }

   }

   static {
      CONFIG = new MenuConfig(PLUGIN, "menus/premadekit_menu.yml");
   }
}
