package dev.viper.utils.message;

import org.bukkit.scheduler.BukkitTask;
import dev.viper.utils.scheduler.Schedulers;
import dev.viper.utils.server.Servers;
import dev.viper.utils.text.color.TextStyle;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBarMessager {
   private final Player player;

   public ActionBarMessager(@NotNull Player player) {
      this.player = player;
   }

   public static @NotNull ActionBarMessager of(@NotNull Player player) {
      return new ActionBarMessager(player);
   }

   public void send(@NotNull Component message) {
      this.player.sendActionBar(message);
   }

   public void clear() {
      this.send(TextStyle.component(" "));
   }

   public void send(@NotNull Component text, int after, int every) {
      Schedulers.async().execute((Consumer<BukkitTask>)((task) -> this.send(text)), after, every);
   }

   public void broadcast(@NotNull Component text) {
      Servers.online().forEach((to) -> to.sendActionBar(text));
   }
}
