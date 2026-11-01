package dev.viper.zbkits.api;

import dev.viper.utils.menu.xmenu.GuiBuilder;
import org.bukkit.entity.Player;

public interface PremadeKitAPI {
   void save(Player player);

   void set(GuiBuilder inventory);

   void load(Player player);

   void saveAll();
}
