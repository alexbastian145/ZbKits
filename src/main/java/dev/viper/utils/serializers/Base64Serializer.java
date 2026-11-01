package dev.viper.utils.serializers;

import dev.viper.utils.item.ItemBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Base64Serializer {
   public String serializeItemStacks(Map<Integer, ItemStack> itemStacks) {
      ItemStack[] items = (ItemStack[])itemStacks.values().toArray(new ItemStack[0]);
      List<String> serializedItems = new ArrayList();

      for(ItemStack item : items) {
         String serialized = this.serializeItemStack(item);
         serializedItems.add(serialized);
      }

      return String.join(";", serializedItems);
   }

   public String serializeItemBuilders(Map<Integer, ItemBuilder> itemBuilders) {
      Map<Integer, ItemStack> itemStacks = new HashMap();

      for(int i = 0; i < itemBuilders.size(); ++i) {
         for(ItemBuilder builder : itemBuilders.values()) {
            ItemStack itemStack = builder.build();
            itemStacks.put(i, itemStack);
         }
      }

      return this.serializeItemStacks(itemStacks);
   }

   public String serializeItemStacks(List<ItemStack> itemStacks) {
      Map<Integer, ItemStack> itemStacksMap = new HashMap();

      for(int i = 0; i < itemStacks.size(); ++i) {
         for(ItemStack stack : itemStacks) {
            itemStacksMap.put(i, stack);
         }
      }

      return this.serializeItemStacks(itemStacksMap);
   }

   public String serializeItemBuilders(List<ItemBuilder> itemBuilders) {
      Map<Integer, ItemStack> itemStacksMap = new HashMap();

      for(int i = 0; i < itemBuilders.size(); ++i) {
         for(ItemBuilder itemBuilder : itemBuilders) {
            ItemStack itemStack = itemBuilder.build();
            itemStacksMap.put(i, itemStack);
         }
      }

      return this.serializeItemStacks(itemStacksMap);
   }

   public String serializeItemStacks(ItemStack[] itemStacks) {
      List<ItemStack> itemStacksList = Arrays.stream(itemStacks).toList();
      return this.serializeItemStacks(itemStacksList);
   }

   public String serializeItemStacks(ItemBuilder[] itemBuilders) {
      List<ItemBuilder> itemBuildersList = Arrays.stream(itemBuilders).toList();
      return this.serializeItemBuilders(itemBuildersList);
   }

   public Map<Integer, ItemStack> deserializeItemStackMap(String data) {
      Map<Integer, ItemStack> kitContents = new HashMap();
      String[] serializedItems = data.split(";");

      for(int i = 0; i < serializedItems.length; ++i) {
         ItemStack item = this.deserialize(serializedItems[i]);
         kitContents.put(i, item);
      }

      return kitContents;
   }

   public Map<Integer, ItemBuilder> deserializeItemBuilderMap(String data) {
      Map<Integer, ItemBuilder> itemBuilders = new HashMap();

      for(int i = 0; i < this.deserializeItemStackMap(data).size(); ++i) {
         for(ItemStack itemStack : this.deserializeItemStackMap(data).values()) {
            itemBuilders.put(i, ItemBuilder.item(itemStack));
         }
      }

      return itemBuilders;
   }

   public List<ItemStack> deserializeItemStackList(String data) {
      return this.deserializeItemStackMap(data).values().stream().toList();
   }

   public List<ItemBuilder> deserializeItemBuilderList(String data) {
      return this.deserializeItemBuilderMap(data).values().stream().toList();
   }

   public String serializeItemStack(ItemStack itemStack) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      try {
         BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
         bukkitObjectOutputStream.writeObject(itemStack);
         bukkitObjectOutputStream.close();
         return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
   }

   public String serializeItemBuilder(ItemBuilder itemBuilder) {
      return this.serializeItemStack(itemBuilder.build());
   }

   public ItemStack deserialize(String data) {
      byte[] bytes = Base64.getDecoder().decode(data);
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

      try {
         BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
         ItemStack itemStack = (ItemStack)bukkitObjectInputStream.readObject();
         bukkitObjectInputStream.close();
         return itemStack;
      } catch (ClassNotFoundException | IOException e) {
         ((Exception)e).printStackTrace();
         return null;
      }
   }
}
