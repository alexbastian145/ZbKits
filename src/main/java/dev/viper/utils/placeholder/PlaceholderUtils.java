package dev.viper.utils.placeholder;

public class PlaceholderUtils {
   public static String fill(String text, String... replacements) {
      for(int i = 0; i < replacements.length; ++i) {
         text = text.replace("{" + i + "}", replacements[i]);
      }

      return text;
   }

   public static PlaceholderBuilder builder() {
      return new PlaceholderBuilder();
   }
}
