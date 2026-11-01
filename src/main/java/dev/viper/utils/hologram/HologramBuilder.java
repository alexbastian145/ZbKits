package dev.viper.utils.hologram;

import dev.viper.utils.location.LocationUtils;
import dev.viper.utils.world.Worlds;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class HologramBuilder {
   private List<Component> lines;
   private Location location;
   private double lineHeight = (double)0.25F;
   private boolean visible = true;
   private boolean deleted = false;

   public HologramBuilder() {
      World world = Worlds.world("world");
      double y = world.getHighestBlockAt(0, 0).getLocation().clone().add((double)0.0F, (double)4.0F, (double)0.0F).getY();
      this.location = new Location(world, (double)0.0F, y, (double)0.0F);
      this.lines = new ArrayList();
   }

   public HologramBuilder(@NotNull Location location) {
      this.location = location;
      this.lines = new ArrayList();
   }

   public static @NotNull HologramBuilder hologram() {
      return new HologramBuilder();
   }

   public static @NotNull HologramBuilder hologram(@NotNull Location location) {
      return new HologramBuilder(location);
   }

   public @NotNull HologramBuilder lines(Component... lines) {
      this.lines = Arrays.asList(lines);
      return this;
   }

   public @NotNull HologramBuilder lines(@NotNull List<Component> lines) {
      this.lines = lines;
      return this;
   }

   public @NotNull HologramBuilder location(@NotNull Location location) {
      this.location = location;
      return this;
   }

   public @NotNull HologramBuilder lineHeight(double lineHeight) {
      this.lineHeight = lineHeight;
      return this;
   }

   public @NotNull HologramBuilder visible(boolean visible) {
      this.visible = visible;
      return this;
   }

   public @NotNull HologramBuilder delete() {
      Objects.requireNonNull(this.location, "Location for hologram cannot be null");
      Objects.requireNonNull(this.lines, "Lines of hologram cannot be null");
      Objects.requireNonNull(this.location.getWorld(), "World of hologram's location cannot be null");
      ArmorStand toDelete = null;

      for(ArmorStand stand : this.location.getWorld().getEntitiesByClass(ArmorStand.class)) {
         if (stand.getLocation() == this.location && stand.getArrowCooldown() == 1) {
            toDelete = stand;
            break;
         }
      }

      Objects.requireNonNull(toDelete, "No hologram found at <location>. Seems to be a NPE".replaceAll("<location>", LocationUtils.serialize(this.location)));
      toDelete.remove();
      this.deleted = true;
      return this;
   }

   public @NotNull HologramBuilder build() {
      Objects.requireNonNull(this.location, "Location for hologram cannot be null");
      Objects.requireNonNull(this.lines, "Lines of hologram cannot be null");
      if (this.deleted) {
         throw new NullPointerException("This hologram has previously been deleted therefore it was not found.");
      } else {
         for(Component text : this.lines) {
            Objects.requireNonNull(this.location.getWorld(), "World of hologram's location cannot be null");
            ArmorStand stand = (ArmorStand)this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
            stand.setGravity(false);
            stand.setInvisible(true);
            stand.setInvulnerable(true);
            stand.setCustomNameVisible(true);
            stand.customName(text);
            stand.setSilent(true);
            stand.setAI(false);
            stand.setArrowCooldown(1);
            stand.setBasePlate(false);
            stand.setCanPickupItems(false);
            stand.setCollidable(false);
            stand.setPersistent(true);
            this.location = this.location.subtract((double)0.0F, this.lineHeight, (double)0.0F);
         }

         return this;
      }
   }
}
