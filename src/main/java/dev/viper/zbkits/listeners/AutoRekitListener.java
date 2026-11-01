package dev.viper.zbkits.listeners;

import dev.viper.zbkits.zbKits;
import dev.viper.utils.event.crystal.PlayerCrystalDeathEvent;
import dev.viper.utils.text.color.ColorizeText;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class AutoRekitListener implements Listener {
   public static void load(Player player) {
      String kit = zbKits.getAutoRekitUtil().getKit(player);
      zbKits.getKitUtil().load(player, kit);
      if (zbKits.getInstance().getConfig().getBoolean("messages.send_auto_rekit_message", true)) {
         String msg = zbKits.getInstance().getConfig().getString("messages.auto_rekit_message");
         if (msg != null) {
            player.sendMessage(ColorizeText.hex(msg));
         }

      }
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent e) {
      Player player = e.getPlayer();
      if (!zbKits.getAutoRekitUtil().hasAutoRekit(player)) {
         zbKits.getAutoRekitUtil().set(player, false, "Kit 1");
      }

   }

   @EventHandler
   public void onDeath(PlayerDeathEvent e) {
      Player victim = e.getEntity();
      Player attacker = victim.getKiller();
      if (attacker != null && zbKits.getAutoRekitUtil().isEnabled(attacker)) {
         load(attacker);
      }

   }

   @EventHandler
   public void onCrystalDeath(PlayerCrystalDeathEvent e) {
      Player victim = e.victim();
      Player attacker = victim.getKiller();
      if (attacker != null && zbKits.getAutoRekitUtil().isEnabled(attacker)) {
         load(attacker);
      }

   }
}
