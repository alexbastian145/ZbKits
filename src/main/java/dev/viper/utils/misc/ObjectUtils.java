package dev.viper.utils.misc;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectUtils {
   @CanIgnoreReturnValue
   public static <T> T nonNull(@Nullable T value) {
      if (value == null) {
         throw new NullPointerException();
      } else {
         return value;
      }
   }

   @CanIgnoreReturnValue
   public static <T> T nonNull(@Nullable T value, @NotNull String text) {
      if (value == null) {
         throw new NullPointerException(text);
      } else {
         return value;
      }
   }

   public static <T> T defaultIfNull(@Nullable T value, T defaultValue) {
      return (T)(value != null ? value : defaultValue);
   }

   public static <T> void ifNonNull(@Nullable T value, Consumer<T> action) {
      if (value != null) {
         action.accept(value);
      }

   }

   @CanIgnoreReturnValue
   public static <T, E extends RuntimeException> T nonNull(@Nullable T value, Supplier<E> exceptionSupplier) {
      if (value == null) {
         throw (RuntimeException)exceptionSupplier.get();
      } else {
         return value;
      }
   }

   @CanIgnoreReturnValue
   public static <T> Collection<T> nonNullElements(@Nullable Collection<T> collection) {
      if (collection != null && !collection.contains((Object)null)) {
         return collection;
      } else {
         throw new NullPointerException("Collection contains null elements");
      }
   }

   @CanIgnoreReturnValue
   public static String nonEmpty(@Nullable String value, @NotNull String fieldName) {
      if (value != null && !value.trim().isEmpty()) {
         return value;
      } else {
         throw new IllegalArgumentException(fieldName + " must not be empty");
      }
   }

   public static <T> Collection<T> emptyIfNull(@Nullable Collection<T> collection) {
      return (Collection<T>)(collection != null ? collection : Collections.emptyList());
   }

   @CanIgnoreReturnValue
   public static <T> T check(@Nullable T value, Predicate<T> predicate, String errorMessage) {
      if (!predicate.test(value)) {
         throw new IllegalArgumentException(errorMessage);
      } else {
         return value;
      }
   }

   public static int requireNonNegative(int value, String errorMessage) {
      if (value < 0) {
         throw new IllegalArgumentException(errorMessage);
      } else {
         return value;
      }
   }
}
