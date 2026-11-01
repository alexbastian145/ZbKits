package dev.viper.utils.location;

import dev.viper.utils.world.Worlds;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class LocationBuilder {
   private double x;
   private double y;
   private double z;
   private float pitch;
   private float yaw;
   private World world;

   public LocationBuilder() {
      this.x = (double)0.0F;
      this.y = (double)64.0F;
      this.z = (double)0.0F;
      this.world = Worlds.world("world");
      this.pitch = 0.0F;
      this.yaw = 0.0F;
   }

   public LocationBuilder(double x, double y, double z, @NotNull World world) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.world = world;
   }

   public LocationBuilder(double x, double y, double z, @NotNull World world, float pitch, float yaw) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.world = world;
      this.pitch = pitch;
      this.yaw = yaw;
   }

   public static @NotNull LocationBuilder location() {
      return new LocationBuilder();
   }

   public static @NotNull LocationBuilder location(double x, double y, double z, @NotNull World world) {
      return new LocationBuilder(x, y, z, world);
   }

   public static @NotNull LocationBuilder location(double x, double y, double z, @NotNull World world, float pitch, float yaw) {
      return new LocationBuilder(x, y, z, world, pitch, yaw);
   }

   public @NotNull LocationBuilder x(double x) {
      this.x = x;
      return this;
   }

   public @NotNull LocationBuilder y(double y) {
      this.y = y;
      return this;
   }

   public @NotNull LocationBuilder z(double z) {
      this.z = z;
      return this;
   }

   public @NotNull LocationBuilder world(@NotNull World world) {
      this.world = world;
      return this;
   }

   public @NotNull LocationBuilder pitch(float pitch) {
      this.pitch = pitch;
      return this;
   }

   public @NotNull LocationBuilder yaw(float yaw) {
      this.yaw = yaw;
      return this;
   }

   public @NotNull Location build() {
      return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
   }
}
