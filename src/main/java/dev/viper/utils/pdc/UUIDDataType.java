package dev.viper.utils.pdc;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class UUIDDataType implements PersistentDataType<byte[], UUID> {
   public static UUIDDataType of() {
      return new UUIDDataType();
   }

   public @NotNull Class<byte[]> getPrimitiveType() {
      return byte[].class;
   }

   public @NotNull Class<UUID> getComplexType() {
      return UUID.class;
   }

   public byte @NotNull [] toPrimitive(UUID complex, @NotNull PersistentDataAdapterContext context) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
      byteBuffer.putLong(complex.getMostSignificantBits());
      byteBuffer.putLong(complex.getLeastSignificantBits());
      return byteBuffer.array();
   }

   public @NotNull UUID fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(primitive);
      long firstLong = byteBuffer.getLong();
      long secondLong = byteBuffer.getLong();
      return new UUID(firstLong, secondLong);
   }
}
