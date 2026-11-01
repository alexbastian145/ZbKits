package dev.viper.utils.message;

import dev.viper.utils.server.Servers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatMessager {
   private final Player player;

   public ChatMessager(@NotNull Player player) {
      this.player = player;
   }

   public static @NotNull ChatMessager of(@NotNull Player player) {
      return new ChatMessager(player);
   }

   public void send(@NotNull Component text) {
      this.player.sendMessage(text);
   }

   public void broadcast(@NotNull Component text) {
      Servers.online().forEach((to) -> to.sendMessage(text));
   }
}
