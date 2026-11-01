package dev.viper.utils.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Storable<T> {
   @Nullable String repr();

   @Nullable T ref(@NotNull String var1);
}
