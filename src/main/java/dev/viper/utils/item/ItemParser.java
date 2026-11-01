package dev.viper.utils.item;

import dev.viper.utils.compat.Compat;
import dev.viper.utils.menu.xmenu.ItemBuilderGUI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ItemParser {
   public static ItemStack parseItem(FileConfiguration config, String configName) {
      ConfigurationSection itemConfig = config.getConfigurationSection(configName);
      if (itemConfig == null) {
         return null;
      } else {
         Material itemMaterial = Material.matchMaterial(itemConfig.getString("material"));
         if (itemMaterial == null) {
            return null;
         } else {
            String itemName = itemConfig.getString("name", "");
            List<String> finalLore = itemConfig.getStringList("lore");
            List<ItemFlag> flags = itemConfig.getStringList("flags").stream().map(ItemFlag::valueOf).toList();
            Map<Enchantment, Integer> enchantments = (Map)itemConfig.getConfigurationSection("enchants").getKeys(false).stream().map((key) -> Compat.enchantment(key)).filter(Objects::nonNull).collect(Collectors.toMap((enchantment) -> enchantment, (enchantment) -> itemConfig.getInt("enchants." + enchantment.getKey().getKey())));
            ItemStack itemStack = (new ItemBuilderGUI(itemMaterial)).name(itemName).lore(finalLore).flags((ItemFlag[])flags.toArray(new ItemFlag[0])).build();
            Objects.requireNonNull(itemStack);
            enchantments.forEach(itemStack::addUnsafeEnchantment);
            return itemStack;
         }
      }
   }
}
