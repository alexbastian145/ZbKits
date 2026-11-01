package dev.viper.zbkits.hooks.worldguard;

import dev.viper.zbkits.zbKits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class WorldGuardHook {
   public static WorldGuardHook get() {
      return new WorldGuardHook();
   }

   public void of() {
      Plugin worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
      if (worldGuardPlugin != null) {
         FileConfiguration config = zbKits.getInstance().getConfig();
         config.addDefault("blacklisted_regions", new String[]{"swordffa", "netheritepotion"});
         config.options().copyDefaults(true);
         zbKits.getInstance().saveConfig();
      }

   }

   public boolean isEnabled() {
      return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
   }
}
