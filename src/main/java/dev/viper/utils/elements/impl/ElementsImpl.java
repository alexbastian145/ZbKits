package dev.viper.utils.elements.impl;

import dev.viper.utils.cachable.Cachable;
import dev.viper.utils.consumers.PairConsumer;
import dev.viper.utils.elements.Elements;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementsImpl<E> implements Elements<E>, Iterable<E> {
   private final Cachable<Integer, E> internal = Cachable.<Integer, E>of();

   public void elements(@NotNull Collection<E> elements) {
      for(E element : elements) {
         this.internal.cache(this.internal.cached(), element);
      }

   }

   @SafeVarargs
   public final void elements(E... elements) {
      this.elements(Arrays.asList(elements));
   }

   public void element(@Nullable E element) {
      this.internal.cache(this.internal.cached(), element);
   }

   public void element(int at, @Nullable E element) {
      this.internal.cache(at, element);
   }

   public @Nullable E element(int at) {
      return this.internal.val(at);
   }

   public int at(@NotNull E element) {
      return (Integer)this.internal.key(element);
   }

   public void del(@NotNull E element) {
      this.internal.del(this.internal.key(element), element);
   }

   public void del(int at) {
      this.internal.del(at);
   }

   public void del(int at, @NotNull E element) {
      this.internal.del(at, element);
   }

   public void delAll(@NotNull Collection<E> elements) {
      for(E element : elements) {
         this.internal.del(this.internal.cached(), element);
      }

   }

   @SafeVarargs
   public final void delAll(E... elements) {
      this.delAll(Arrays.asList(elements));
   }

   public boolean has(@NotNull E element) {
      return this.internal.hasVal(element);
   }

   public @NotNull Iterator<E> iterator() {
      return this.internal.snapshot().asMap().values().iterator();
   }

   public void forEach(Consumer<? super E> action) {
      for(E element : this.internal.snapshot().asMap().values()) {
         action.accept(element);
      }

   }

   public void forEach(@NotNull PairConsumer<Integer, E> elementConsumer) {
      this.internal.forEach(elementConsumer);
   }

   public @NotNull Collection<E> elements() {
      return this.internal.snapshot().asMap().values();
   }

   public @NotNull E[] elementsArray() {
      return (E[])this.elements().toArray();
   }

   public void clear() {
      this.internal.clear();
   }

   public int size() {
      return this.internal.cached();
   }

   public void delAllNull() {
      this.forEach((PairConsumer)((at, what) -> {
         if (what == null) {
            this.del(at);
         }

      }));
   }

   public ElementsImpl<E> sortKeys() {
      List<E> sorted = this.internal.snapshot().asMap().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getValue).toList();
      return Elements.of(sorted);
   }
}
