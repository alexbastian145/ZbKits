package dev.viper.zbkits.utils;

import dev.viper.zbkits.zbKits;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;

/** Safe material lookup for menu files, so one bad entry can't break a whole menu. */
public final class Materials {
   private static final Set<String> WARNED = ConcurrentHashMap.newKeySet();

   private Materials() {
   }

   /**
    * Resolves a material name from a menu file. Unknown names (typos, or materials renamed or
    * removed in another Minecraft version) log one warning and show as a barrier block instead of throwing.
    */
   public static Material parse(String name) {
      Material material = name == null ? null : Material.matchMaterial(name.trim());
      if (material == null) {
         if (WARNED.add(String.valueOf(name))) {
            zbKits.getInstance().getLogger().warning("Unknown material '" + name + "' in a menu file, showing a barrier instead.");
         }

         return Material.BARRIER;
      }

      return material;
   }
}
