package dev.viper.zbkits.utils;

import dev.viper.utils.text.color.ColorizeText;
import dev.viper.zbkits.api.AutoRekitAPI;
import dev.viper.zbkits.utils.config.MenuConfig;
import dev.viper.zbkits.utils.data.PlayerDataManager;
import dev.viper.zbkits.zbKits;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/** Auto rekit settings, stored in the player's data file under "auto_rekit". */
public class AutoRekitUtil implements AutoRekitAPI {
   private final PlayerDataManager data;
   private final MenuConfig CONFIG = new MenuConfig(zbKits.getInstance(), "menus/autorekit_menu.yml");

   public AutoRekitUtil(PlayerDataManager data) {
      this.data = data;
   }

   public void set(Player player, Boolean enabled, String kitName) {
      UUID id = player.getUniqueId();
      YamlConfiguration config = this.data.get(id);
      config.set("auto_rekit.enabled", enabled);
      config.set("auto_rekit.kit", kitName);
      this.data.save(id, config);
   }

   public void setKit(Player player, String kitName) {
      UUID id = player.getUniqueId();
      YamlConfiguration config = this.data.get(id);
      config.set("auto_rekit.kit", kitName);
      this.data.save(id, config);
   }

   public void toggle(Player player) {
      UUID id = player.getUniqueId();
      YamlConfiguration config = this.data.get(id);
      config.set("auto_rekit.enabled", !config.getBoolean("auto_rekit.enabled", false));
      this.data.save(id, config);
   }

   public boolean get(Player player) {
      return this.data.get(player.getUniqueId()).getBoolean("auto_rekit.enabled", false);
   }

   public String getKit(Player player) {
      return this.data.get(player.getUniqueId()).getString("auto_rekit.kit", "Kit 1");
   }

   public boolean isEnabled(Player player) {
      return this.get(player);
   }

   public boolean hasAutoRekit(Player player) {
      return this.data.get(player.getUniqueId()).contains("auto_rekit");
   }

   public String status(Player player) {
      boolean autoRekitEnabled = this.get(player);
      String enabled = this.CONFIG.getConfig().getString("auto_rekit.placeholders.enabled", "&#7cff6eEnabled");
      String disabled = this.CONFIG.getConfig().getString("auto_rekit.placeholders.disabled", "&#ffa6a6Disabled");
      return autoRekitEnabled ? ColorizeText.hex(enabled) : ColorizeText.hex(disabled);
   }
}
