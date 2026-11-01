package dev.viper.zbkits.api;

import dev.viper.utils.menu.xmenu.GuiBuilder;
import org.bukkit.entity.Player;

public interface KitsAPI {
   void save(Player player, String kitName);

   void load(Player player, String kitName);

   void set(Player player, String kitName, GuiBuilder inventory);

   void importInventory(Player player, GuiBuilder inventory);

   void delete(Player player, String kitName);

   boolean exists(Player player, String kitName);

   void saveAll();
}
