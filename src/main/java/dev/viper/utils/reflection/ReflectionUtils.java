package dev.viper.utils.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import org.bukkit.Bukkit;

public class ReflectionUtils {
   public static final String NM_PACKAGE = "net.minecraft";
   public static final String CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit";
   public static final String NMS_PACKAGE = "net.minecraft.server";
   public static final String VERSION = craftBukkitVersion();
   private static final MethodType VOID_METHOD_TYPE;
   private static final boolean NMS_REPACKAGED;
   private static volatile Object theUnsafe;

   private static String craftBukkitVersion() {
      String packageName = Bukkit.getServer().getClass().getPackage().getName();
      return packageName.length() > "org.bukkit.craftbukkit".length() + 1 ? packageName.substring("org.bukkit.craftbukkit".length() + 1) : "";
   }

   public static boolean compare(Class<?> a, Class<?> b) {
      return a.getCanonicalName().equals(b.getCanonicalName());
   }

   public static boolean isRepackaged() {
      return NMS_REPACKAGED;
   }

   public static String nmsClassName(String post1_17package, String className) {
      if (NMS_REPACKAGED) {
         String classPackage = post1_17package == null ? "net.minecraft" : "net.minecraft." + post1_17package;
         return classPackage + "." + className;
      } else {
         return "net.minecraft.server." + VERSION + "." + className;
      }
   }

   public static Class<?> nmsClass(String post1_17package, String className) throws ClassNotFoundException {
      return Class.forName(nmsClassName(post1_17package, className));
   }

   public static Optional<Class<?>> nmsOptionalClass(String post1_17package, String className) {
      return optionalClass(nmsClassName(post1_17package, className));
   }

   public static String obcClassName(String className) {
      return "org.bukkit.craftbukkit." + VERSION + "." + className;
   }

   public static Class<?> obcClass(String className) throws ClassNotFoundException {
      return Class.forName(obcClassName(className));
   }

   public static Optional<Class<?>> obcOptionalClass(String className) {
      return optionalClass(obcClassName(className));
   }

   public static Optional<Class<?>> optionalClass(String className) {
      try {
         return Optional.of(Class.forName(className));
      } catch (ClassNotFoundException var2) {
         return Optional.empty();
      }
   }

   public static Object enumValueOf(Class<?> enumClass, String enumName) {
      return Enum.valueOf(enumClass.asSubclass(Enum.class), enumName);
   }

   public static Object enumValueOf(Class<?> enumClass, String enumName, int fallbackOrdinal) {
      try {
         return enumValueOf(enumClass, enumName);
      } catch (IllegalArgumentException e) {
         Object[] constants = enumClass.getEnumConstants();
         if (constants.length > fallbackOrdinal) {
            return constants[fallbackOrdinal];
         } else {
            throw e;
         }
      }
   }

   public static Class<?> innerClass(Class<?> parentClass, Predicate<Class<?>> classPredicate) throws ClassNotFoundException {
      for(Class<?> innerClass : parentClass.getDeclaredClasses()) {
         if (classPredicate.test(innerClass)) {
            return innerClass;
         }
      }

      throw new ClassNotFoundException("No class in " + parentClass.getCanonicalName() + " matches the predicate.");
   }

   public static PacketConstructor findPacketConstructor(Class<?> packetClass, MethodHandles.Lookup lookup) throws Exception {
      try {
         MethodHandle constructor = lookup.findConstructor(packetClass, VOID_METHOD_TYPE);
         Objects.requireNonNull(constructor);
         return constructor::invoke;
      } catch (IllegalAccessException | NoSuchMethodException var7) {
         if (theUnsafe == null) {
            synchronized(ReflectionUtils.class) {
               if (theUnsafe == null) {
                  Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                  Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
                  theUnsafeField.setAccessible(true);
                  theUnsafe = theUnsafeField.get((Object)null);
               }
            }
         }

         MethodType allocateMethodType = MethodType.methodType(Object.class, Class.class);
         MethodHandle allocateMethod = lookup.findVirtual(theUnsafe.getClass(), "allocateInstance", allocateMethodType);
         return () -> allocateMethod.invoke(theUnsafe, packetClass);
      }
   }

   static {
      VOID_METHOD_TYPE = MethodType.methodType(Void.TYPE);
      NMS_REPACKAGED = optionalClass("net.minecraft.network.protocol.Packet").isPresent();
   }

   @FunctionalInterface
   public interface PacketConstructor {
      Object invoke() throws Throwable;
   }
}
