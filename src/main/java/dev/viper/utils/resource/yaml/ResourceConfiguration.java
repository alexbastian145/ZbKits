package dev.viper.utils.resource.yaml;

import dev.viper.utils.cachable.Cachable;
import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.model.Tuple;
import dev.viper.utils.resource.ResourceFile;
import dev.viper.utils.text.color.TextStyle;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public record ResourceConfiguration(ResourceFile resourceFile) {
   public Object val(String key) {
      return this.bukkit().get(key);
   }

   public String valAsString(String key) {
      return this.bukkit().getString(key);
   }

   public boolean valAsBoolean(String key) {
      return this.bukkit().getBoolean(key);
   }

   public List<Boolean> valAsBooleanList(String key) {
      return this.bukkit().getBooleanList(key);
   }

   public List<Byte> valAsByteList(String key) {
      return this.bukkit().getByteList(key);
   }

   public List<Character> valAsCharacterList(String key) {
      return this.bukkit().getCharacterList(key);
   }

   public Color valAsColor(String key) {
      return this.bukkit().getColor(key);
   }

   public double valAsDouble(String key) {
      return this.bukkit().getDouble(key);
   }

   public List<Double> valAsDoubleList(String key) {
      return this.bukkit().getDoubleList(key);
   }

   public List<Float> valAsFloatList(String key) {
      return this.bukkit().getFloatList(key);
   }

   public int valAsInt(String key) {
      return this.bukkit().getInt(key);
   }

   public List<Integer> valAsIntList(String key) {
      return this.bukkit().getIntegerList(key);
   }

   public ItemStack valAsItemStack(String key) {
      return this.bukkit().getItemStack(key);
   }

   public List<?> valAsList(String key) {
      return this.bukkit().getList(key);
   }

   public Location valAsLocation(String key) {
      return this.bukkit().getLocation(key);
   }

   public long valAsLong(String key) {
      return this.bukkit().getLong(key);
   }

   public List<Long> valAsLongList(String key) {
      return this.bukkit().getLongList(key);
   }

   public List<Map<?, ?>> valAsMapList(String key) {
      return this.bukkit().getMapList(key);
   }

   public <T> T val(String key, Class<T> clazz) {
      if (clazz.getCanonicalName().equals(Component.class.getCanonicalName())) {
         return (T)clazz.cast(this.valAsComponent(key));
      } else {
         return (T)(clazz.getCanonicalName().equals(ItemBuilder.class.getCanonicalName()) ? clazz.cast(this.valAsItemBuilder(key)) : this.bukkit().getObject(key, clazz));
      }
   }

   public OfflinePlayer valAsOfflinePlayer(String key) {
      return this.bukkit().getOfflinePlayer(key);
   }

   public List<Short> valAsShortList(String key) {
      return this.bukkit().getShortList(key);
   }

   public List<String> valAsStringList(String key) {
      return this.bukkit().getStringList(key);
   }

   public Vector valAsVector(String key) {
      return this.bukkit().getVector(key);
   }

   public ItemBuilder valAsItemBuilder(String key) {
      return ItemBuilder.item(this.valAsItemStack(key));
   }

   public Component valAsComponent(String key) {
      return TextStyle.style(this.valAsString(key));
   }

   public void set(String key, Object val) {
      this.bukkit().set(key, val);
      this.resourceFile.saveYml();
   }

   public void set(Tuple<String, Object> tuple) {
      if (tuple != null && tuple.key() != null) {
         this.set((String)Objects.requireNonNull(tuple.key()), tuple.val());
      }

   }

   public Cachable<String, Object> keyVal() {
      Cachable<String, Object> cachable = Cachable.<String, Object>of();

      for(String key : this.keys()) {
         Object val = this.bukkit().get(key);
         cachable.cache(key, val);
      }

      return cachable;
   }

   public Collection<String> keys() {
      return this.bukkit().getKeys(false);
   }

   public YamlConfiguration bukkit() {
      return (YamlConfiguration)this.resourceFile.loadYml();
   }
}
