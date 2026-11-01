package dev.viper.utils.tierlist;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Tiers {
   private static Tiers instance;
   private final JavaPlugin plugin;
   private static final String API_URL = "https://mctiers.com/api/profile/";

   private Tiers(JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public static void initialize(JavaPlugin plugin) {
      instance = new Tiers(plugin);
   }

   public @NotNull CompletableFuture<String> getTierAsync(Player player, String tierName) {
      return CompletableFuture.supplyAsync(() -> {
         String id = player.getUniqueId().toString().replace("-", "");
         String url = String.format("%s%s", "https://mctiers.com/api/profile/", id);

         try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String var18;
            label55: {
               String tierValue;
               try {
                  StringBuilder response = new StringBuilder();

                  String inputLine;
                  while((inputLine = in.readLine()) != null) {
                     response.append(inputLine);
                  }

                  Gson gson = new Gson();
                  JsonObject jsonResponse = (JsonObject)gson.fromJson(response.toString(), JsonObject.class);
                  JsonObject rankings = jsonResponse.getAsJsonObject("rankings");
                  JsonObject tierObject = rankings.getAsJsonObject(tierName);
                  if (tierObject != null) {
                     int tierValue = tierObject.get("tier").getAsInt();
                     int pos = tierObject.get("pos").getAsInt();
                     boolean retiredBoolean = tierObject.get("retired").getAsBoolean();
                     String retired = retiredBoolean ? "R" : "";
                     String position = pos == 0 ? "HT" : "LT";
                     var18 = retired + position + tierValue;
                     break label55;
                  }

                  tierValue = "n/a";
               } catch (Throwable var20) {
                  try {
                     in.close();
                  } catch (Throwable var19) {
                     var20.addSuppressed(var19);
                  }

                  throw var20;
               }

               in.close();
               return tierValue;
            }

            in.close();
            return var18;
         } catch (Exception e) {
            e.printStackTrace();
            return "welp, seems like we ran into an error d:";
         }
      });
   }

   public static Tiers get() {
      Objects.requireNonNull(instance, "Tiers instance has not been initialized. Make sure to call initialize() method first.");
      return instance;
   }
}
