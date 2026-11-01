package dev.viper.utils.text.color;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class ColorizeText {
   private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]){6}");
   private static final Pattern MM_HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");
   private static final Pattern MM_PATTERN = Pattern.compile("<([a-zA-Z0-9_]+)>");
   private static final Pattern LEGACY_PATTERN = Pattern.compile("&([a-fA-F0-9])");
   private static final Map<String, ChatColor> COLOR_MAP = new HashMap();
   private static final Map<String, ChatColor> DECORATION_MAP = new HashMap();

   private ColorizeText() {
   }

   public static String hex(String message) {
      message = ChatColor.translateAlternateColorCodes('&', message);
      Matcher matcher = HEX_PATTERN.matcher(message);

      while(matcher.find()) {
         String hex = message.substring(matcher.start(), matcher.end());
         String color = ChatColor.of("#" + hex.substring(2)).toString();
         message = message.replace(hex, color);
         matcher.reset(message);
      }

      return message;
   }

   public static String mm(String message) {
      Matcher matcher = MM_PATTERN.matcher(message);

      while(matcher.find()) {
         String format = message.substring(matcher.start() + 1, matcher.end() - 1).toLowerCase();
         ChatColor replacement = (ChatColor)COLOR_MAP.get(format);
         if (replacement == null) {
            replacement = (ChatColor)DECORATION_MAP.get(format);
         }

         if (replacement != null) {
            message = message.replace("<" + format + ">", replacement.toString());
            matcher.reset(message);
         }
      }

      Matcher hexMatcher = MM_HEX_PATTERN.matcher(message);

      while(hexMatcher.find()) {
         String hex = message.substring(hexMatcher.start() + 2, hexMatcher.end() - 1);
         String color = ChatColor.of("#" + hex).toString();
         message = message.replace("<#" + hex + ">", color);
         hexMatcher.reset(message);
      }

      return message;
   }

   public static String legacy(String message) {
      Matcher matcher = LEGACY_PATTERN.matcher(message);

      while(matcher.find()) {
         String legacy = message.substring(matcher.start(), matcher.end());
         ChatColor color = ChatColor.getByChar(legacy.charAt(1));
         message = message.replace(legacy, color.toString());
         matcher.reset(message);
      }

      return message;
   }

   static {
      COLOR_MAP.put("black", ChatColor.BLACK);
      COLOR_MAP.put("dark_blue", ChatColor.DARK_BLUE);
      COLOR_MAP.put("dark_green", ChatColor.DARK_GREEN);
      COLOR_MAP.put("dark_aqua", ChatColor.DARK_AQUA);
      COLOR_MAP.put("dark_red", ChatColor.DARK_RED);
      COLOR_MAP.put("dark_purple", ChatColor.DARK_PURPLE);
      COLOR_MAP.put("gold", ChatColor.GOLD);
      COLOR_MAP.put("gray", ChatColor.GRAY);
      COLOR_MAP.put("dark_gray", ChatColor.DARK_GRAY);
      COLOR_MAP.put("blue", ChatColor.BLUE);
      COLOR_MAP.put("green", ChatColor.GREEN);
      COLOR_MAP.put("aqua", ChatColor.AQUA);
      COLOR_MAP.put("red", ChatColor.RED);
      COLOR_MAP.put("light_purple", ChatColor.LIGHT_PURPLE);
      COLOR_MAP.put("yellow", ChatColor.YELLOW);
      COLOR_MAP.put("white", ChatColor.WHITE);
      COLOR_MAP.put("reset", ChatColor.RESET);
      DECORATION_MAP.put("bold", ChatColor.BOLD);
      DECORATION_MAP.put("strikethrough", ChatColor.STRIKETHROUGH);
      DECORATION_MAP.put("underline", ChatColor.UNDERLINE);
      DECORATION_MAP.put("italic", ChatColor.ITALIC);
      DECORATION_MAP.put("magic", ChatColor.MAGIC);
   }
}
