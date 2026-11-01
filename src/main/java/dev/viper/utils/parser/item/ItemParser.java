package dev.viper.utils.parser.item;

import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.model.Tuple;
import dev.viper.utils.text.color.TextStyle;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

public class ItemParser {
   public static @NotNull ItemBuilder parse(@NotNull FileConfiguration configuration, @NotNull String path) {
      return parse(configuration, path, new ArrayList());
   }

   public static @NotNull ItemBuilder parse(@NotNull FileConfiguration configuration, @NotNull String path, @NotNull List<Tuple<String, String>> replacements) {
      ConfigurationSection section = configuration.getConfigurationSection(path);
      if (section == null) {
         return ItemBuilder.item(Material.AIR);
      } else {
         String rawMaterial = section.getString("material");
         if (rawMaterial == null) {
            rawMaterial = "barrier";
         }

         String stringMaterial = rawMaterial.replaceAll(" ", "_").replaceAll("minecraft:", "").toUpperCase();
         Material material = Material.matchMaterial(stringMaterial);
         if (material == null) {
            material = Material.BARRIER;
         }

         ItemBuilder builder = ItemBuilder.item(material);
         String skull = section.getString("skull");
         if (material == Material.PLAYER_HEAD && skull != null && skull.startsWith("uuid:")) {
            String skullArg = skull.replaceAll("uuid:", "");

            for(Tuple<String, String> replacementPair : replacements) {
               String find = replacementPair.key();
               String replace = replacementPair.val();
               if (find != null && replace != null) {
                  skullArg = skullArg.replaceAll("<" + find + ">", replace);
               }
            }

            builder.skullOwner(skullArg);
         }

         String name = section.getString("name");
         if (name == null) {
            name = path;
         }

         for(Tuple<String, String> replacementPair : replacements) {
            String find = replacementPair.key();
            String replace = replacementPair.val();
            if (find != null && replace != null) {
               name = name.replaceAll("<" + find + ">", replace);
            }
         }

         Component displayName = TextStyle.style(name);
         builder.name(displayName);
         int amount = section.getInt("amount");
         if (amount == 0) {
            amount = 1;
         }

         builder.amount(amount);
         List<String> rawLore = section.getStringList("lore");
         List<String> copiedLore = new ArrayList();

         for(String line : rawLore) {
            for(Tuple<String, String> replacementPair : replacements) {
               String find = replacementPair.key();
               String replace = replacementPair.val();
               if (find != null && replace != null) {
                  line = line.replaceAll("<" + find + ">", replace);
               }
            }

            copiedLore.add(line);
         }

         List<Component> componentLore = TextStyle.style(copiedLore);
         builder.lore(componentLore);
         boolean glow = section.getBoolean("glow");
         if (glow) {
            builder.glow();
         } else {
            builder.removeGlow();
         }

         ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");
         if (enchantmentSection != null) {
            List<Tuple<Enchantment, Integer>> enchantmentsToAdd = new ArrayList();

            for(String key : enchantmentSection.getKeys(false)) {
               for(Enchantment possible : Registry.ENCHANTMENT.stream().toList()) {
                  if (possible.getKey().value().equals(key.toLowerCase())) {
                     enchantmentsToAdd.add(enchantmentSection.getInt(key + ".level") == 0 ? Tuple.tuple(possible, 1) : Tuple.tuple(possible, enchantmentSection.getInt(key + ".level")));
                  }
               }
            }

            for(Tuple<Enchantment, Integer> pair : enchantmentsToAdd) {
               Enchantment key = pair.key();
               Integer val = pair.val();
               if (key != null && val != null) {
                  builder.addUnsafeEnchantment(key, val);
               }
            }
         }

         for(String rawFlag : section.getStringList("flags")) {
            ItemFlag flag;
            try {
               flag = ItemFlag.valueOf(rawFlag.toUpperCase());
            } catch (IllegalArgumentException var22) {
               continue;
            }

            builder.addFlag(flag);
         }

         Integer customModelData = (Integer)section.get("custom_model_data");
         if (customModelData != null && section.getKeys(false).contains("custom_model_data")) {
            builder.customModelData(customModelData);
         }

         boolean unbreakable = section.getBoolean("unbreakable");
         if (unbreakable) {
            builder.unbreakable();
         }

         return builder;
      }
   }
}
