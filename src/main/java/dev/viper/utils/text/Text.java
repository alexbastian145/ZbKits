package dev.viper.utils.text;

import dev.viper.utils.text.color.TextStyle;
import net.kyori.adventure.text.Component;

public record Text(String string) {
   public static Text text(String string) {
      return new Text(string);
   }

   public Component component() {
      return TextStyle.color(this.string);
   }

   public String colored() {
      return TextStyle.legacy(this.string);
   }

   public String toString() {
      return this.string;
   }
}
