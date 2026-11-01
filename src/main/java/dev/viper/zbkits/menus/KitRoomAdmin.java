package dev.viper.zbkits.menus;

import dev.viper.zbkits.zbKits;
import dev.viper.utils.menu.xmenu.GuiBuilder;
import dev.viper.utils.menu.xmenu.ItemBuilderGUI;
import dev.viper.utils.text.color.ColorizeText;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitRoomAdmin extends GuiBuilder {
   public KitRoomAdmin() {
      super(54);
   }

   public static void openAdminKitRoom(Player player, String category) {
      GuiBuilder inventory = new GuiBuilder(54, "Editing " + category + " Kit Room");

      for(int i = 1; i <= 9; ++i) {
         int slot = i + 44;
         ItemStack filter = (new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)).name(" ").flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
         inventory.setItem(slot, filter);
      }

      zbKits.getKitRoomUtil().load(inventory, category);
      ItemStack save = (new ItemBuilderGUI(Material.LIME_DYE)).name(ColorizeText.mm("<green>Save")).flags(ItemFlag.HIDE_ATTRIBUTES).build();
      inventory.setItem(48, save, (p) -> {
         zbKits.getKitRoomUtil().save(player, category);
         KitsMenu.openKitMenu(player).open(player);
      });
      ItemStack reset = (new ItemBuilderGUI(Material.RED_DYE)).name(ColorizeText.mm("<red>Reset")).flags(ItemFlag.HIDE_ATTRIBUTES).build();
      inventory.setItem(50, reset, (p) -> {
         for(int i1 = 0; i1 < 45; ++i1) {
            inventory.setItem(i1, new ItemStack(Material.AIR));
         }

      });
      ItemStack back = (new ItemBuilderGUI(Material.RED_STAINED_GLASS_PANE)).name(ColorizeText.mm("<#ffa6a6>Back")).flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).build();
      inventory.setItem(49, back, (p) -> KitsMenu.openKitMenu(player).open(player));
      inventory.addClickHandler((event) -> {
         int slot = event.getRawSlot();
         if (slot >= 0 && slot <= 44) {
            event.setCancelled(false);
         }

      });
      inventory.open(player);
   }
}
