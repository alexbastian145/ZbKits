package dev.viper.utils.config.val;

import dev.viper.utils.item.ItemBuilder;
import dev.viper.utils.library.Utils;
import dev.viper.utils.text.Text;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ConfigList(@NotNull ConfigVal val) {
   public static @NotNull ConfigList list(@NotNull ConfigVal value) {
      return new ConfigList(value);
   }

   public List<ItemStack> itemStacks() {
      return (List<ItemStack>)(List<?>)Utils.plugin().getConfig().getList(this.val.path);
   }

   public List<ItemBuilder> itemBuilders() {
      List<ItemBuilder> itemBuilders = new ArrayList();

      for(ItemStack itemStack : this.itemStacks()) {
         itemBuilders.add(ItemBuilder.item(itemStack));
      }

      return itemBuilders;
   }

   public List<Text> texts() {
      List<Text> texts = new ArrayList();

      for(String string : Utils.plugin().getConfig().getStringList(this.val.path)) {
         texts.add(Text.text(string));
      }

      return texts;
   }

   public List<Component> components() {
      List<Component> components = new ArrayList();

      for(Text text : this.texts()) {
         components.add(text.component());
      }

      return components;
   }
}
