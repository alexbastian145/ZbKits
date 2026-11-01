package dev.viper.utils.prettify;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListPrettify {
   public static @NotNull String strings(List<String> input) {
      return input.toString().replaceAll("\\[", "").replaceAll("]", "");
   }

   public static @NotNull String players(List<Player> input) {
      List<String> inputToString = (List)input.stream().map(Player::getName).collect(Collectors.toList());
      return strings(inputToString);
   }
}
