package dev.viper.utils.cachable.impl;

import dev.viper.utils.cachable.Cachable;
import dev.viper.utils.cachable.CachableSnapshot;
import dev.viper.utils.consumers.PairConsumer;
import dev.viper.utils.model.Tuple;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class CachableImpl<K, V> implements Cachable<K, V> {
   final Map<K, V> cache = new ConcurrentHashMap();

   public V val(K key) {
      return (V)this.cache.get(key);
   }

   public K key(V val) {
      for(Map.Entry<K, V> entry : this.cache.entrySet()) {
         if (entry.getValue().equals(val)) {
            return (K)entry.getKey();
         }
      }

      return null;
   }

   public boolean hasKey(K key) {
      return this.cache.containsKey(key);
   }

   public boolean hasVal(V val) {
      return this.cache.containsValue(val);
   }

   public void cache(K key, V val) {
      this.cache.put(key, val);
   }

   public void del(K key, V val) {
      if (this.cache.get(key).equals(val)) {
         this.cache.remove(key);
      }

   }

   public void del(K key) {
      this.cache.remove(key);
   }

   public void cacheAll(Collection<Tuple<K, V>> entries) {
      for(Tuple<K, V> entry : entries) {
         this.cache(entry.key(), entry.val());
      }

   }

   public void cacheAll(Map<K, V> entries) {
      for(Map.Entry<K, V> entry : entries.entrySet()) {
         K key = (K)entry.getKey();
         V val = (V)entry.getValue();
         this.cache(key, val);
      }

   }

   public void delAll(Collection<K> keys) {
      for(K key : keys) {
         this.cache.remove(key);
      }

   }

   public void forEach(PairConsumer<K, V> forEach) {
      Map<K, V> var10000 = this.cache;
      Objects.requireNonNull(forEach);
      var10000.forEach(forEach::execute);
   }

   public int cached() {
      return this.cache.size();
   }

   public void clear() {
      this.cache.clear();
   }

   public @NotNull CachableSnapshot<K, V> snapshot() {
      return new CachableSnapshotImpl<K, V>(this);
   }
}
