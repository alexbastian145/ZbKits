package dev.viper.utils.commodore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;

final class ReflectionUtil {
   private static final String SERVER_VERSION = getServerVersion();

   private static String getServerVersion() {
      Class<?> server = Bukkit.getServer().getClass();
      if (!server.getSimpleName().equals("CraftServer")) {
         return ".";
      } else if (server.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
         return ".";
      } else {
         String version = server.getName().substring("org.bukkit.craftbukkit".length());
         return version.substring(0, version.length() - "CraftServer".length());
      }
   }

   public static String mc(String name) {
      return "net.minecraft." + name;
   }

   public static String nms(String className) {
      return "net.minecraft.server" + SERVER_VERSION + className;
   }

   public static Class<?> mcClass(String className) throws ClassNotFoundException {
      return Class.forName(mc(className));
   }

   public static Class<?> nmsClass(String className) throws ClassNotFoundException {
      return Class.forName(nms(className));
   }

   public static String obc(String className) {
      return "org.bukkit.craftbukkit" + SERVER_VERSION + className;
   }

   public static Class<?> obcClass(String className) throws ClassNotFoundException {
      return Class.forName(obc(className));
   }

   public static int minecraftVersion() {
      try {
         Matcher matcher = Pattern.compile("\\(MC: (\\d+)\\.(\\d+)\\.?(\\d+?)?( .*)?\\)").matcher(Bukkit.getVersion());
         if (matcher.find()) {
            int major = Integer.parseInt(matcher.group(1), 10);
            // "1.<minor>" for the old scheme; the new "<year>.<drop>" scheme (26.1, 26.2, ...) is always newer
            return major == 1 ? Integer.parseInt(matcher.group(2), 10) : major;
         } else {
            throw new IllegalArgumentException(String.format("No match found in '%s'", Bukkit.getVersion()));
         }
      } catch (IllegalArgumentException ex) {
         throw new RuntimeException("Failed to determine Minecraft version", ex);
      }
   }

   private ReflectionUtil() {
   }
}
