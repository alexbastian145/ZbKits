package dev.viper.zbkits.commands;

import dev.viper.zbkits.zbKits;
import dev.viper.zbkits.utils.Messages;
import dev.viper.zbkits.menus.KitRoomAdmin;
import dev.viper.utils.command.ZarbunexCommand;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitRoomAdminCommand extends ZarbunexCommand<zbKits> {
   private static final List<String> CATEGORIES = Arrays.asList("CRYSTAL_PVP", "POTIONS", "BOWS_ARROWS", "MISC");
   private static final List<String> ACTIONS = Arrays.asList("add", "clear");

   public KitRoomAdminCommand(zbKits plugin) {
      super(plugin, "zbkits", "kitroomadmin");
      this.addTabbComplete(0, new String[]{"add"});
      this.addTabbComplete(0, new String[]{"clear"});
      this.addTabbComplete(1, new String[]{"CRYSTAL_PVP"});
      this.addTabbComplete(1, new String[]{"POTIONS"});
      this.addTabbComplete(1, new String[]{"BOWS_ARROWS"});
      this.addTabbComplete(1, new String[]{"MISC"});
      this.setPermission("zbkits.admin");
      this.setUsage("");
      this.registerCommand();
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (sender instanceof Player player) {
         if (args.length != 2 && player.hasPermission("zbkits.admin")) {
            Messages.send(sender, "kitroomadmin_usage");
            return false;
         } else {
            String action = args[0].toLowerCase();
            String category = args[1].toUpperCase();
            if (CATEGORIES.contains(category) && ACTIONS.contains(action)) {
               if ("add".equals(action)) {
                  KitRoomAdmin.openAdminKitRoom(player, category);
               } else if ("clear".equals(action)) {
                  zbKits.getKitRoomUtil().delete(category);
                  Messages.send(player, "kitroom_category_cleared", "%category%", category);
               }

               return true;
            } else {
               Messages.send(sender, "kitroomadmin_invalid");
               return false;
            }
         }
      } else {
         return true;
      }
   }
}
