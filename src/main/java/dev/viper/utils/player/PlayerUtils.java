package dev.viper.utils.player;

import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.compat.Compat;
import dev.viper.utils.library.Utils;
import dev.viper.utils.message.ActionBarMessager;
import dev.viper.utils.message.ChatMessager;
import dev.viper.utils.message.Messagers;
import dev.viper.utils.text.color.TextStyle;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerUtils {
   public static void reset(@NotNull Player player) {
      player.setFlying(false);
      player.setAllowFlight(false);
      player.setGameMode(GameMode.SURVIVAL);
      player.setLevel(0);
      player.setExp(0.0F);
      player.setTotalExperience(0);
      player.setFoodLevel(20);
      player.setHealth((double)20.0F);
      player.getInventory().clear();
   }

   public static void clearChat(@NotNull Player player, int count) {
      Stream<String> var10000 = IntStream.range(0, count).mapToObj((i) -> "");
      Objects.requireNonNull(player);
      var10000.forEach(player::sendMessage);
   }

   public static void hide(@NotNull Player target) {
      Utils.plugin().getServer().getOnlinePlayers().forEach((player) -> player.hidePlayer(Utils.plugin(), target));
   }

   public static void show(@NotNull Player target) {
      Bukkit.getOnlinePlayers().forEach((player) -> player.showPlayer(Utils.plugin(), target));
   }

   public static void heal(@NotNull Player player) {
      player.setSaturation(20.0F);
      Attribute maxHealth = Compat.maxHealth();
      AttributeInstance maxHealthInstance = maxHealth == null ? null : player.getAttribute(maxHealth);
      if (maxHealthInstance != null) {
         player.setHealth(maxHealthInstance.getValue());
      }

      player.setArrowsInBody(0);
      player.setFoodLevel(20);
   }

   public static void title(@NotNull Player player, @NotNull Component title, @NotNull Component subtitle, @NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
      Audience audience = Audience.audience(new Audience[]{player});
      audience.showTitle(Title.title(title, subtitle, Times.times(fadeIn, stay, fadeOut)));
   }

   public static void broadcastTitle(@NotNull Component title, @NotNull Component subtitle, @NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
      Bukkit.getOnlinePlayers().forEach((player) -> title(player, title, subtitle, fadeIn, stay, fadeOut));
   }

   public static void teleport(@NotNull Player player, @NotNull Location location) {
      player.teleport(location);
   }

   public static void teleport(@NotNull Player player, @NotNull Player target) {
      player.teleport(target.getLocation());
   }

   public static void giveItem(@NotNull Player player, @NotNull ItemBuilder item) {
      player.getInventory().addItem(new ItemStack[]{item.build()});
   }

   public static void clearInventory(@NotNull Player player) {
      player.getInventory().clear();
   }

   public static void applyPotionEffect(@NotNull Player player, @NotNull PotionEffectType type, int duration, int amplifier) {
      player.addPotionEffect(new PotionEffect(type, duration, amplifier));
   }

   public static void removePotionEffect(@NotNull Player player, @NotNull PotionEffectType type) {
      player.removePotionEffect(type);
   }

   public static void clearPotionEffects(@NotNull Player player) {
      Stream<org.bukkit.potion.PotionEffectType> var10000 = player.getActivePotionEffects().stream().map(PotionEffect::getType);
      Objects.requireNonNull(player);
      var10000.forEach(player::removePotionEffect);
   }

   public static void removeItem(@NotNull Player player, @NotNull ItemBuilder item) {
      player.getInventory().removeItem(new ItemStack[]{item.build()});
   }

   public static void gamemodeOfPlayers(@NotNull GameMode gamemode, @NotNull List<Player> players) {
      players.forEach((player) -> player.setGameMode(gamemode));
   }

   public static @NotNull Player player(@NotNull Player player) {
      return player;
   }

   public static @Nullable Player player(@NotNull UUID uuid) {
      return Utils.plugin().getServer().getPlayer(uuid);
   }

   public static @Nullable Player player(@NotNull String playerName) {
      return Utils.plugin().getServer().getPlayer(playerName);
   }

   public static @NotNull OfflinePlayer offline(@NotNull UUID uuid) {
      return Utils.plugin().getServer().getOfflinePlayer(uuid);
   }

   public static void chat(@NotNull Player player, @NotNull String text) {
      Messagers.chat(player).send(TextStyle.component(text));
   }

   public static void chat(@NotNull Player player, @NotNull Component text) {
      Messagers.chat(player).send(text);
   }

   public static @NotNull ChatMessager chatMessager(@NotNull Player player) {
      return Messagers.chat(player);
   }

   public static void actionBar(@NotNull Player player, @NotNull String text) {
      Messagers.actionBar(player).send(TextStyle.component(text));
   }

   public static void actionBar(@NotNull Player player, @NotNull Component text) {
      Messagers.actionBar(player).send(text);
   }

   public static @NotNull ActionBarMessager actionBarMessager(@NotNull Player player) {
      return Messagers.actionBar(player);
   }
}
