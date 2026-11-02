package dev.viper.utils.serializers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ByteSerializer {
   public byte[] serialize(ItemStack item) {
      return item.serializeAsBytes();
   }

   public byte[] serialize(List<ItemStack> items) {
      return this.serialize((ItemStack[])items.toArray(new ItemStack[0]));
   }

   public byte[] serialize(ItemStack[] items) {
      ByteArrayDataOutput stream = ByteStreams.newDataOutput();

      for(ItemStack item : items) {
         byte[] data = item != null && item.getType() != Material.AIR ? item.serializeAsBytes() : new byte[0];
         stream.writeInt(data.length);
         stream.write(data);
      }

      stream.writeInt(-1);
      return stream.toByteArray();
   }

   public ItemStack deserialize(byte[] bytes) {
      return ItemStack.deserializeBytes(bytes);
   }

   public ItemStack[] deserializeArray(byte[] bytes) {
      List<ItemStack> items = new ArrayList();
      ByteArrayDataInput inputStream = ByteStreams.newDataInput(bytes);

      for(int length = inputStream.readInt(); length != -1; length = inputStream.readInt()) {
         if (length == 0) {
            items.add(null);
         } else {
            byte[] data = new byte[length];
            inputStream.readFully(data, 0, data.length);
            items.add(ItemStack.deserializeBytes(data));
         }
      }

      return (ItemStack[])items.toArray(new ItemStack[0]);
   }

   public List<ItemStack> deserializeList(byte[] bytes) {
      return Arrays.asList(this.deserializeArray(bytes));
   }
}
