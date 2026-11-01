package dev.viper.utils.cachable;

import dev.viper.utils.model.Tuple;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CachableSnapshot<K, V> {
   Map<K, V> asMap();

   List<Tuple<K, V>> asList();

   Collection<Tuple<K, V>> asCollection();

   Tuple<K, V>[] asTupleArray();
}
