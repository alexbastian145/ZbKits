package dev.viper.zbkits.utils;

import dev.viper.utils.text.color.ColorizeText;
import dev.viper.zbkits.zbKits;
import org.bukkit.command.CommandSender;

/**
 * Reads player-facing messages from the "messages" section of config.yml.
 * Missing keys fall back to the defaults bundled in the jar. Setting a message to "" disables it.
 */
public final class Messages {
   private Messages() {
   }

   /** Returns the coloured message for the key, with placeholder/value pairs replaced, or null if the key is missing. */
   public static String get(String key, String... replacements) {
      String raw = zbKits.getInstance().getConfig().getString("messages." + key);
      if (raw == null) {
         return null;
      }

      for(int i = 0; i + 1 < replacements.length; i += 2) {
         raw = raw.replace(replacements[i], replacements[i + 1]);
      }

      return ColorizeText.hex(raw);
   }

   public static void send(CommandSender to, String key, String... replacements) {
      String message = get(key, replacements);
      if (message != null && !message.isEmpty()) {
         to.sendMessage(message);
      }

   }
}
