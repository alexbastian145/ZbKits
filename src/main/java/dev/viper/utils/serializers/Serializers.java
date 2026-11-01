package dev.viper.utils.serializers;

import org.jetbrains.annotations.NotNull;

public class Serializers {
   public static @NotNull Base64Serializer base64() {
      return new Base64Serializer();
   }

   public static @NotNull ByteSerializer bytes() {
      return new ByteSerializer();
   }
}
