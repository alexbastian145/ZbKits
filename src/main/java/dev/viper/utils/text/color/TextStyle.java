package dev.viper.utils.text.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;

public class TextStyle {
   public static String legacyHex(String text) {
      String pattern = "<#([A-Fa-f0-9]{6})>";
      Pattern colorPattern = Pattern.compile(pattern);
      Matcher matcher = colorPattern.matcher(text);
      StringBuilder modifiedMessage = new StringBuilder();

      while(matcher.find()) {
         String hexColor = matcher.group(1);
         ChatColor color = ChatColor.of("#" + hexColor);
         matcher.appendReplacement(modifiedMessage, color.toString());
      }

      matcher.appendTail(modifiedMessage);
      return modifiedMessage.toString();
   }

   public static String legacy(String text) {
      text = text.replaceAll("<black>", ChatColor.BLACK.toString());
      text = text.replaceAll("<dark_blue>", ChatColor.DARK_BLUE.toString());
      text = text.replaceAll("<dark_green>", ChatColor.DARK_GREEN.toString());
      text = text.replaceAll("<dark_aqua>", ChatColor.DARK_AQUA.toString());
      text = text.replaceAll("<dark_light_blue>", ChatColor.DARK_AQUA.toString());
      text = text.replaceAll("<dark_red>", ChatColor.DARK_RED.toString());
      text = text.replaceAll("<dark_purple>", ChatColor.DARK_PURPLE.toString());
      text = text.replaceAll("<dark_pink>", ChatColor.DARK_PURPLE.toString());
      text = text.replaceAll("<gold>", ChatColor.GOLD.toString());
      text = text.replaceAll("<dark_yellow>", ChatColor.GOLD.toString());
      text = text.replaceAll("<gray>", ChatColor.GRAY.toString());
      text = text.replaceAll("<grey>", ChatColor.GRAY.toString());
      text = text.replaceAll("<dark_gray>", ChatColor.DARK_GRAY.toString());
      text = text.replaceAll("<dark_grey>", ChatColor.DARK_GRAY.toString());
      text = text.replaceAll("<blue>", ChatColor.BLUE.toString());
      text = text.replaceAll("<green>", ChatColor.GREEN.toString());
      text = text.replaceAll("<lime>", ChatColor.GREEN.toString());
      text = text.replaceAll("<aqua>", ChatColor.AQUA.toString());
      text = text.replaceAll("<light_blue>", ChatColor.AQUA.toString());
      text = text.replaceAll("<red>", ChatColor.RED.toString());
      text = text.replaceAll("<pink>", ChatColor.LIGHT_PURPLE.toString());
      text = text.replaceAll("<purple>", ChatColor.LIGHT_PURPLE.toString());
      text = text.replaceAll("<light_purple>", ChatColor.LIGHT_PURPLE.toString());
      text = text.replaceAll("<light_pink>", ChatColor.LIGHT_PURPLE.toString());
      text = text.replaceAll("<magenta>", ChatColor.LIGHT_PURPLE.toString());
      text = text.replaceAll("<yellow>", ChatColor.YELLOW.toString());
      text = text.replaceAll("<light_yellow>", ChatColor.YELLOW.toString());
      text = text.replaceAll("<white>", ChatColor.WHITE.toString());
      text = text.replaceAll("<bold>", ChatColor.BOLD.toString());
      text = text.replaceAll("<reset>", ChatColor.RESET.toString());
      text = text.replaceAll("<underline>", ChatColor.UNDERLINE.toString());
      text = text.replaceAll("<italic>", ChatColor.ITALIC.toString());
      text = text.replaceAll("<strikethrough>", ChatColor.STRIKETHROUGH.toString());
      text = text.replaceAll("<strike>", ChatColor.STRIKETHROUGH.toString());
      text = text.replaceAll("<obfuscated>", ChatColor.MAGIC.toString());
      text = text.replaceAll("<magic>", ChatColor.MAGIC.toString());
      text = text.replaceAll("<random>", ChatColor.MAGIC.toString());
      text = text.replaceAll("<newline>", "\n");
      text = text.replaceAll("<nl>", "\n");
      text = legacyTranslate(text);
      text = legacyHex(text);
      return text;
   }

   public static Component component(String text) {
      MiniMessage miniMessage = MiniMessage.miniMessage();
      return miniMessage.deserialize(text).decoration(TextDecoration.ITALIC, false);
   }

   public static List<Component> component(List<String> texts) {
      List<Component> list = new ArrayList();

      for(String text : texts) {
         list.add(component(text));
      }

      return list;
   }

   public static List<Component> component(String... texts) {
      return component(Arrays.asList(texts));
   }

   public static Component color(String text) {
      return component(text);
   }

   public static List<Component> color(List<String> texts) {
      return component(texts);
   }

   public static List<Component> color(String... texts) {
      return component(texts);
   }

   public static List<Component> style(List<String> texts) {
      return component(texts);
   }

   public static List<Component> style(String... texts) {
      return component(texts);
   }

   public static Component style(String text) {
      return component(text);
   }

   public Component joined(Collection<Component> values) {
      return Component.join(JoinConfiguration.newlines(), values);
   }

   public Component joined(Component... values) {
      return Component.join(JoinConfiguration.newlines(), values);
   }

   public Component joined(Iterable<? extends ComponentLike> values) {
      return Component.join(JoinConfiguration.newlines(), values);
   }

   public static String text(Component component) {
      MiniMessage miniMessage = MiniMessage.miniMessage();
      return (String)miniMessage.serialize(component);
   }

   public static String legacyTranslate(String text) {
      return ChatColor.translateAlternateColorCodes('&', text);
   }

   public static ChatColor chatColor(String input) {
      char character;
      if (input.startsWith("&")) {
         character = input.charAt(1);
      } else {
         character = input.charAt(0);
      }

      return ChatColor.getByChar(character);
   }

   public static String value(ChatColor color) {
      return color.toString();
   }

   public static ChatColor hex(String hex) {
      return !hex.startsWith("#") ? ChatColor.of("#" + hex) : ChatColor.of(hex);
   }
}
