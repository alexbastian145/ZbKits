package dev.viper.utils.scheduler;

import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class TickTimes {
   public static long SECOND = oneSecond();
   public static long MINUTE = oneMinute();
   public static long HOUR = oneHour();
   public static long DAY = oneDay();

   public static long oneSecond() {
      return 20L;
   }

   public static long oneMinute() {
      return 1200L;
   }

   public static long oneHour() {
      return 72000L;
   }

   public static long oneDay() {
      return 1728000L;
   }

   public static long secondsToTicks(int seconds) {
      return (long)seconds * oneSecond();
   }

   public static long minutesToTicks(int minutes) {
      return (long)minutes * oneMinute();
   }

   public static long hoursToTicks(int hours) {
      return (long)hours * oneHour();
   }

   public static long daysToTicks(int days) {
      return (long)days * oneDay();
   }

   public static long secondsToTicks(long seconds) {
      return seconds * oneSecond();
   }

   public static long minutesToTicks(long minutes) {
      return minutes * oneMinute();
   }

   public static long hoursToTicks(long hours) {
      return hours * oneHour();
   }

   public static long daysToTicks(long days) {
      return days * oneDay();
   }

   public static long ticksToSeconds(int ticks) {
      return (long)ticks / oneSecond();
   }

   public static long ticksToMinutes(int ticks) {
      return (long)ticks / oneMinute();
   }

   public static long ticksToHours(int ticks) {
      return (long)ticks / oneHour();
   }

   public static long ticksToDays(int ticks) {
      return (long)ticks / oneDay();
   }

   public static long ticksToSeconds(long ticks) {
      return ticks / oneSecond();
   }

   public static long ticksToMinutes(long ticks) {
      return ticks / oneMinute();
   }

   public static long ticksToHours(long ticks) {
      return ticks / oneHour();
   }

   public static long ticksToDays(long ticks) {
      return ticks / oneDay();
   }

   public static long ticks(int val, @NotNull TimeUnit unit) {
      long var10000;
      switch (unit) {
         case NANOSECONDS:
         case MILLISECONDS:
         case MICROSECONDS:
            throw new UnsupportedOperationException();
         case SECONDS:
            var10000 = secondsToTicks(val);
            break;
         case MINUTES:
            var10000 = minutesToTicks(val);
            break;
         case HOURS:
            var10000 = hoursToTicks(val);
            break;
         case DAYS:
            var10000 = daysToTicks(val);
            break;
         default:
            throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public static int val(long ticks, @NotNull TimeUnit unit) {
      int var10000;
      switch (unit) {
         case NANOSECONDS:
         case MILLISECONDS:
         case MICROSECONDS:
            throw new UnsupportedOperationException();
         case SECONDS:
            var10000 = (int)ticksToSeconds(ticks);
            break;
         case MINUTES:
            var10000 = (int)ticksToMinutes(ticks);
            break;
         case HOURS:
            var10000 = (int)ticksToHours(ticks);
            break;
         case DAYS:
            var10000 = (int)ticksToDays(ticks);
            break;
         default:
            throw new IncompatibleClassChangeError();
      }

      return var10000;
   }
}
