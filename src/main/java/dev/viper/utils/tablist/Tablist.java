package dev.viper.utils.tablist;

import dev.viper.utils.text.color.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.entity.Player;

public class Tablist {
   private static final List<Component> header = new ArrayList();
   private static final List<Component> footer = new ArrayList();

   public static void header(List<Component> header) {
      Tablist.header.clear();
      Tablist.header.addAll(header);
   }

   public static void header(Component... header) {
      header(Arrays.asList(header));
   }

   public static void header(String... header) {
      header(TextStyle.style(header));
   }

   public static void footer(List<Component> footer) {
      Tablist.footer.clear();
      Tablist.footer.addAll(footer);
   }

   public static void footer(Component... footer) {
      footer(Arrays.asList(footer));
   }

   public static void footer(String... footer) {
      footer(TextStyle.style(footer));
   }

   public static void update(Player player) {
      Component joinedHeader = Component.empty();
      Component joinedFooter = Component.empty();
      if (!header.isEmpty()) {
         joinedHeader = Component.join(JoinConfiguration.newlines(), header);
      }

      if (!footer.isEmpty()) {
         joinedFooter = Component.join(JoinConfiguration.newlines(), footer);
      }

      player.sendPlayerListHeaderAndFooter(joinedHeader, joinedFooter);
   }

   public static void update(List<Player> players) {
      for(Player player : players) {
         update(player);
      }

   }

   public static void update(Player... players) {
      for(Player player : players) {
         update(player);
      }

   }
}
