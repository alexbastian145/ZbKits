package dev.viper.utils.text.color;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessages {
   private static final MiniMessage miniMessage = MiniMessage.miniMessage();

   public static void sendMessage(Audience audience, String message) {
      audience.sendMessage(miniMessage.deserialize(message));
   }

   public static void sendActionBar(Audience audience, String message) {
      audience.sendActionBar(miniMessage.deserialize(message));
   }

   public static Component parseMiniMessage(String message) {
      return miniMessage.deserialize(message);
   }

   public static String serialize(Component component) {
      return (String)miniMessage.serialize(component);
   }

   public static Component deserialize(String input) {
      return miniMessage.deserialize(input);
   }
}
