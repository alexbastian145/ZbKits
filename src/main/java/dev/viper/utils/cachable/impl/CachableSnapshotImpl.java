package dev.viper.utils.cachable.impl;

import dev.viper.utils.cachable.CachableSnapshot;
import dev.viper.utils.model.Tuple;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CachableSnapshotImpl<K, V> implements CachableSnapshot<K, V> {
   private final CachableImpl<K, V> cachable;

   CachableSnapshotImpl(CachableImpl<K, V> cachable) {
      this.cachable = cachable;
   }

   public Map<K, V> asMap() {
      return this.cachable.cache;
   }

   public List<Tuple<K, V>> asList() {
      List<Tuple<K, V>> list = new ArrayList();

      for(K key : this.cachable.cache.keySet()) {
         for(V val : this.cachable.cache.values()) {
            list.add(Tuple.tuple(key, val));
         }
      }

      return list;
   }

   public Collection<Tuple<K, V>> asCollection() {
      return this.asList();
   }

   public Tuple<K, V>[] asTupleArray() {
      Tuple<K, V>[] tuples = (Tuple[])Array.newInstance(Tuple.class, this.asList().size());
      int i = 0;

      for(Tuple<K, V> tuple : this.asList()) {
         tuples[i++] = tuple;
      }

      return tuples;
   }
}
