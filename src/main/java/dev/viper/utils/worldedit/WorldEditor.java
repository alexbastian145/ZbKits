package dev.viper.utils.worldedit;

public class WorldEditor {
   public static AsyncWorldEditor async() {
      return new AsyncWorldEditor();
   }

   public static SyncWorldEditor sync() {
      return new SyncWorldEditor();
   }

   public static AsyncWorldEditor asynchronous() {
      return async();
   }

   public static SyncWorldEditor synchronous() {
      return sync();
   }
}
