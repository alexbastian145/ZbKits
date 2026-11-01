package dev.viper.utils.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tuple<K, V> {
   private K key;
   private V val;

   public Tuple(@Nullable K key, @Nullable V val) {
      this.key = key;
      this.val = val;
   }

   public static <K, V> @NotNull Tuple<K, V> tuple(@Nullable K key, @Nullable V val) {
      return new Tuple<K, V>(key, val);
   }

   public @NotNull String toString() {
      return "[<key> : <val>]".replaceAll("<key>", this.key.toString()).replaceAll("<val>", this.val.toString());
   }

   public @NotNull Tuple<K, V> key(@Nullable K key) {
      this.key = key;
      return this;
   }

   public @NotNull Tuple<K, V> val(@Nullable V val) {
      this.val = val;
      return this;
   }

   public @Nullable K key() {
      return this.key;
   }

   public @Nullable V val() {
      return this.val;
   }
}
