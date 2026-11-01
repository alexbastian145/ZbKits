package dev.viper.utils.elements;

import dev.viper.utils.consumers.PairConsumer;
import dev.viper.utils.elements.impl.ElementsImpl;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Elements<E> extends Iterable<E> {
   static <E> @NotNull ElementsImpl<E> of() {
      return new ElementsImpl<E>();
   }

   static <E> @NotNull ElementsImpl<E> of(@NotNull Class<E> type) {
      return new ElementsImpl<E>();
   }

   static <E> @NotNull ElementsImpl<E> of(@Nullable Collection<E> elements) {
      ElementsImpl<E> elementsImpl = new ElementsImpl<E>();
      elementsImpl.elements(elements);
      return elementsImpl;
   }

   @SafeVarargs
   static <E> @NotNull ElementsImpl<E> of(E... elements) {
      ElementsImpl<E> elementsImpl = new ElementsImpl<E>();
      elementsImpl.elements(elements);
      return elementsImpl;
   }

   void elements(@NotNull Collection<E> var1);

   void elements(E... var1);

   void element(@Nullable E var1);

   void element(int var1, @Nullable E var2);

   @Nullable E element(int var1);

   int at(@NotNull E var1);

   void del(@NotNull E var1);

   void del(int var1);

   void del(int var1, @NotNull E var2);

   void delAll(@NotNull Collection<E> var1);

   void delAll(E... var1);

   boolean has(@NotNull E var1);

   void forEach(Consumer<? super E> var1);

   void forEach(@NotNull PairConsumer<Integer, E> var1);

   Iterator<E> iterator();

   @NotNull Collection<E> elements();

   @NotNull E[] elementsArray();

   void clear();

   int size();
}
