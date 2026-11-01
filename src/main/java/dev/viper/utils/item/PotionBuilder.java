package dev.viper.utils.item;

import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class PotionBuilder {
   private final ItemStack item;
   private final PotionMeta potionMeta;

   public PotionBuilder(ItemStack item) {
      if (!(item.getItemMeta() instanceof PotionMeta)) {
         throw new IllegalArgumentException("ItemMeta is required to be an instance of PotionMeta to use PotionBuilder!");
      } else {
         this.item = item;
         this.potionMeta = (PotionMeta)item.getItemMeta();
      }
   }

   public static PotionBuilder potion(ItemStack item) {
      return new PotionBuilder(item);
   }

   public static PotionBuilder potion(Material material) {
      return potion(new ItemStack(material));
   }

   public PotionBuilder(Material material) {
      this(new ItemStack(material));
   }

   public PotionBuilder meta(Consumer<PotionMeta> consumer) {
      consumer.accept(this.potionMeta);
      return this;
   }

   public PotionBuilder addEffect(PotionEffect effect, boolean override) {
      this.potionMeta.addCustomEffect(effect, override);
      return this;
   }

   public static PotionBuilder consumablePotion() {
      return new PotionBuilder(Material.POTION);
   }

   public ItemStack build() {
      this.item.setItemMeta(this.potionMeta);
      return this.item;
   }
}
