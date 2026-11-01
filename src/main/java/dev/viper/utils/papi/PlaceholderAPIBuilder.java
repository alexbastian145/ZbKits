package dev.viper.utils.papi;

import dev.viper.utils.library.Utils;
import dev.viper.utils.prettify.ListPrettify;
import java.util.function.BiFunction;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIBuilder {
   private final String prefix;
   private String author;
   private String version;
   private BiFunction<Player, String, String> playerResult;
   private BiFunction<OfflinePlayer, String, String> offlinePlayerResult;

   public PlaceholderAPIBuilder(@NotNull String prefix) {
      this.prefix = prefix;
      this.version = Utils.plugin().getPluginMeta().getVersion();
      this.playerResult = null;
      this.offlinePlayerResult = null;
      this.author = ListPrettify.strings(Utils.plugin().getPluginMeta().getAuthors());
   }

   public static @NotNull PlaceholderAPIBuilder of(@NotNull String identifier) {
      return new PlaceholderAPIBuilder(identifier);
   }

   public @NotNull PlaceholderAPIBuilder author(@NotNull String author) {
      this.author = author;
      return this;
   }

   public @NotNull PlaceholderAPIBuilder version(@NotNull String version) {
      this.version = version;
      return this;
   }

   public @NotNull PlaceholderAPIBuilder onlineRequest(@Nullable BiFunction<Player, String, String> result) {
      this.playerResult = result;
      return this;
   }

   public @NotNull PlaceholderAPIBuilder offlineRequest(@Nullable BiFunction<OfflinePlayer, String, String> result) {
      this.offlinePlayerResult = result;
      return this;
   }

   public @NotNull PlaceholderAPIBuilder request(@Nullable BiFunction<Player, String, String> onlineResult, @Nullable BiFunction<OfflinePlayer, String, String> offlineResult) {
      this.playerResult = onlineResult;
      this.offlinePlayerResult = offlineResult;
      return this;
   }

   public @Nullable PlaceholderExpansion build() {
      PlaceholderExpansion expansion = new PlaceholderExpansion() {
         public @NotNull String getIdentifier() {
            return PlaceholderAPIBuilder.this.prefix;
         }

         public @NotNull String getAuthor() {
            return PlaceholderAPIBuilder.this.author;
         }

         public @NotNull String getVersion() {
            return PlaceholderAPIBuilder.this.version;
         }

         public String onPlaceholderRequest(Player player, @NotNull String params) {
            return PlaceholderAPIBuilder.this.playerResult != null ? (String)PlaceholderAPIBuilder.this.playerResult.apply(player, params) : null;
         }

         public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
            return PlaceholderAPIBuilder.this.offlinePlayerResult != null ? (String)PlaceholderAPIBuilder.this.offlinePlayerResult.apply(offlinePlayer, params) : null;
         }
      };
      if (expansion.canRegister() && !expansion.isRegistered()) {
         expansion.register();
         return expansion;
      } else {
         return null;
      }
   }
}
