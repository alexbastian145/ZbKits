package dev.viper.zbkits.api;

import dev.viper.utils.menu.xmenu.GuiBuilder;
import org.bukkit.entity.Player;

public interface KitRoomAPI {
   void save(Player player, String category);

   void load(GuiBuilder inventory, String category);

   void delete(String category);

   boolean exists(String category);
}
