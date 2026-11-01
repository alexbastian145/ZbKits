package dev.viper.zbkits.api;

import org.bukkit.entity.Player;

public interface AutoRekitAPI {
   void set(Player player, Boolean enabled, String kitName);

   void setKit(Player player, String kitName);

   void toggle(Player player);

   boolean get(Player player);

   String getKit(Player player);

   boolean isEnabled(Player player);

   boolean hasAutoRekit(Player player);

   String status(Player player);
}
