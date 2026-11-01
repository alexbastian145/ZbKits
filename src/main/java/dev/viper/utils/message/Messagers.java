package dev.viper.utils.message;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Messagers {
   public static @NotNull ActionBarMessager actionBar(@NotNull Player player) {
      return new ActionBarMessager(player);
   }

   public static @NotNull ChatMessager chat(@NotNull Player player) {
      return new ChatMessager(player);
   }
}
