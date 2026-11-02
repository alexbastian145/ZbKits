package dev.viper.utils.menu.xmenu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemBuilderGUI {
   private final ItemStack item;

   public static ItemBuilderGUI copyOf(ItemStack item) {
      return new ItemBuilderGUI(item.clone());
   }

   public ItemBuilderGUI(Material material) {
      this(new ItemStack(material));
   }

   public ItemBuilderGUI(ItemStack item) {
      this.item = (ItemStack)Objects.requireNonNull(item, "item");
   }

   public ItemBuilderGUI edit(Consumer<ItemStack> function) {
      function.accept(this.item);
      return this;
   }

   public ItemBuilderGUI meta(Consumer<ItemMeta> metaConsumer) {
      return this.edit((item) -> {
         ItemMeta meta = item.getItemMeta();
         if (meta != null) {
            metaConsumer.accept(meta);
            item.setItemMeta(meta);
         }

      });
   }

   public <T extends ItemMeta> ItemBuilderGUI meta(Class<T> metaClass, Consumer<T> metaConsumer) {
      return this.meta((meta) -> {
         if (metaClass.isInstance(meta)) {
            metaConsumer.accept(metaClass.cast(meta));
         }

      });
   }

   public ItemBuilderGUI type(Material material) {
      return this.edit((item) -> item.setType(material));
   }

   public ItemBuilderGUI data(int data) {
      return this.durability((short)data);
   }

   public ItemBuilderGUI durability(short durability) {
      return this.edit((item) -> item.setDurability(durability));
   }

   public ItemBuilderGUI amount(int amount) {
      return this.edit((item) -> item.setAmount(amount));
   }

   public ItemBuilderGUI enchant(Enchantment enchantment) {
      return this.enchant(enchantment, 1);
   }

   public ItemBuilderGUI enchant(Enchantment enchantment, int level) {
      return this.meta((meta) -> meta.addEnchant(enchantment, level, true));
   }

   public ItemBuilderGUI removeEnchant(Enchantment enchantment) {
      return this.meta((meta) -> meta.removeEnchant(enchantment));
   }

   public ItemBuilderGUI removeEnchants() {
      return this.meta((m) -> {
         Set<Enchantment> var10000 = m.getEnchants().keySet();
         Objects.requireNonNull(m);
         var10000.forEach(m::removeEnchant);
      });
   }

   public ItemBuilderGUI name(String name) {
      return this.meta((meta) -> meta.setDisplayName(name));
   }

   public ItemBuilderGUI lore(String lore) {
      return this.lore(Collections.singletonList(lore));
   }

   public ItemBuilderGUI lore(String... lore) {
      return this.lore(Arrays.asList(lore));
   }

   public ItemBuilderGUI lore(List<String> lore) {
      return this.meta((meta) -> meta.setLore(lore));
   }

   public ItemBuilderGUI addLore(String line) {
      return this.meta((meta) -> {
         List<String> lore = meta.getLore();
         if (lore == null) {
            meta.setLore(Collections.singletonList(line));
         } else {
            lore.add(line);
            meta.setLore(lore);
         }
      });
   }

   public ItemBuilderGUI addLore(String... lines) {
      return this.addLore(Arrays.asList(lines));
   }

   public ItemBuilderGUI addLore(List<String> lines) {
      return this.meta((meta) -> {
         List<String> lore = meta.getLore();
         if (lore == null) {
            meta.setLore(lines);
         } else {
            lore.addAll(lines);
            meta.setLore(lore);
         }
      });
   }

   public ItemBuilderGUI flags(ItemFlag... flags) {
      return this.meta((meta) -> meta.addItemFlags(flags));
   }

   public ItemBuilderGUI flags() {
      return this.flags(ItemFlag.values());
   }

   public ItemBuilderGUI removeFlags(ItemFlag... flags) {
      return this.meta((meta) -> meta.removeItemFlags(flags));
   }

   public ItemBuilderGUI removeFlags() {
      return this.removeFlags(ItemFlag.values());
   }

   public ItemBuilderGUI armorColor(Color color) {
      return this.meta(LeatherArmorMeta.class, (meta) -> meta.setColor(color));
   }

   public ItemStack build() {
      return this.item;
   }
}
