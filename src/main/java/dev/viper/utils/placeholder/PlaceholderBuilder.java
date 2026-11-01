package dev.viper.utils.placeholder;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderBuilder {
   private String text;
   private final Map<String, String> placeholders = new HashMap();

   public PlaceholderBuilder text(String text) {
      this.text = text;
      return this;
   }

   public PlaceholderBuilder placeholder(String placeholder, String replacement) {
      this.placeholders.put(placeholder, replacement);
      return this;
   }

   public String build() {
      for(Map.Entry<String, String> entry : this.placeholders.entrySet()) {
         this.text = this.text.replace("{" + (String)entry.getKey() + "}", (CharSequence)entry.getValue());
      }

      return this.text;
   }

   public static PlaceholderBuilder of() {
      return new PlaceholderBuilder();
   }
}
