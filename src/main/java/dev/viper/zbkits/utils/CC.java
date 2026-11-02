package dev.viper.zbkits.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class CC {
   public static String cc(String message) {
      message = ChatColor.translateAlternateColorCodes('&', message);
      Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");

      String colorCode;
      ChatColor color;
      for(Matcher matcher = pattern.matcher(message); matcher.find(); message = message.replace(colorCode, color.toString())) {
         colorCode = matcher.group();
         color = ChatColor.of(colorCode.substring(1));
      }

      return message;
   }
}
