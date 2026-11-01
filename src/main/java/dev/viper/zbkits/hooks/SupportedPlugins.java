package dev.viper.zbkits.hooks;

import dev.viper.zbkits.hooks.worldguard.WorldGuardHook;

public enum SupportedPlugins {
   WORLD_GUARD("WorldGuard") {
      public void hook() {
         WorldGuardHook.get().of();
      }
   };

   private final String pluginName;

   private SupportedPlugins(String pluginName) {
      this.pluginName = pluginName;
   }

   public String getName() {
      return this.pluginName;
   }

   public abstract void hook();

}
