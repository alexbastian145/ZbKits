package dev.viper.utils.menu.listener;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public interface CloseListener {
   void onClose(@NotNull InventoryCloseEvent var1);
}
