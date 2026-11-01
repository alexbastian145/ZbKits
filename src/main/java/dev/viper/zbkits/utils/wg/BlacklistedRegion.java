package dev.viper.zbkits.utils.wg;

import dev.viper.zbkits.zbKits;
import dev.viper.utils.worldguard.WorldGuardUtil;
import org.bukkit.entity.Player;

public class BlacklistedRegion {
   public static boolean isInBlacklistedRegion(Player player) {
      for(String region : zbKits.getInstance().getConfig().getStringList("blacklisted_regions")) {
         if (WorldGuardUtil.isinRegion(player, region)) {
            return true;
         }
      }

      return false;
   }
}
