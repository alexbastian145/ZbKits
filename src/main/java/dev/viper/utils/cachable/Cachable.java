package dev.viper.utils.cachable;

import dev.viper.utils.cachable.impl.CachableImpl;
import dev.viper.utils.consumers.PairConsumer;
import dev.viper.utils.model.Tuple;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public interface Cachable<K, V> {
   static <K, V> CachableImpl<K, V> of() {
      return new CachableImpl<K, V>();
   }

   static <K, V> CachableImpl<K, V> of(Collection<Tuple<K, V>> entries) {
      CachableImpl<K, V> cachable = new CachableImpl<K, V>();
      cachable.cacheAll(entries);
      return cachable;
   }

   static <K, V> CachableImpl<K, V> of(Map<K, V> entries) {
      CachableImpl<K, V> cachable = new CachableImpl<K, V>();
      cachable.cacheAll(entries);
      return cachable;
   }

   @SafeVarargs
   static <K, V> CachableImpl<K, V> of(Tuple<K, V>... entries) {
      return of(Arrays.asList(entries));
   }

   static <K, V> CachableImpl<K, V> of(Class<K> keyType, Class<V> valType) {
      return new CachableImpl<K, V>();
   }

   V val(K var1);

   K key(V var1);

   boolean hasKey(K var1);

   boolean hasVal(V var1);

   void cache(K var1, V var2);

   void del(K var1, V var2);

   void del(K var1);

   void cacheAll(Collection<Tuple<K, V>> var1);

   void cacheAll(Map<K, V> var1);

   void delAll(Collection<K> var1);

   void forEach(PairConsumer<K, V> var1);

   int cached();

   void clear();

   CachableSnapshot<K, V> snapshot();
}
