package dev.viper.utils.compat;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Small helpers that avoid hard-linking against Bukkit constants which were renamed between
 * Minecraft releases (e.g. {@code GENERIC_MAX_HEALTH -> MAX_HEALTH}, {@code LUCK -> LUCK_OF_THE_SEA}).
 * Looking them up at runtime keeps the plugin loadable on 1.21.x as well as on the 26.x releases.
 */
public final class Compat {
   private static Attribute maxHealth;
   private static boolean maxHealthResolved;

   private Compat() {
   }

   /** The max-health attribute, or {@code null} if it can't be resolved. */
   public static synchronized @Nullable Attribute maxHealth() {
      if (!maxHealthResolved) {
         maxHealth = constant(Attribute.class, "MAX_HEALTH", "GENERIC_MAX_HEALTH");
         maxHealthResolved = true;
      }

      return maxHealth;
   }

   /** Looks an enchantment up by its vanilla key (e.g. {@code "mending"}), case-insensitive. */
   public static @Nullable Enchantment enchantment(@NotNull String key) {
      NamespacedKey namespacedKey = NamespacedKey.minecraft(key.toLowerCase());
      return Registry.ENCHANTMENT.get(namespacedKey);
   }

   /** The enchantment previously exposed as {@code Enchantment.LUCK}; used for the "glow" effect. */
   public static @Nullable Enchantment luckOfTheSea() {
      return enchantment("luck_of_the_sea");
   }

   /** Reads the first public static constant of {@code type} that exists under one of {@code names}. */
   private static <T> @Nullable T constant(@NotNull Class<T> type, @NotNull String... names) {
      for(String name : names) {
         try {
            Object value = type.getField(name).get((Object)null);
            if (type.isInstance(value)) {
               return type.cast(value);
            }
         } catch (ReflectiveOperationException | RuntimeException var6) {
         }
      }

      return null;
   }
}
