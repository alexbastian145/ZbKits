package dev.viper.utils.commodore;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface Commodore {
   void register(Command var1, LiteralCommandNode<?> var2, Predicate<? super Player> var3);

   default void register(Command command, LiteralArgumentBuilder<?> argumentBuilder, Predicate<? super Player> permissionTest) {
      Objects.requireNonNull(command, "command");
      Objects.requireNonNull(argumentBuilder, "argumentBuilder");
      Objects.requireNonNull(permissionTest, "permissionTest");
      this.register(command, argumentBuilder.build(), permissionTest);
   }

   default void register(Command command, LiteralCommandNode<?> node) {
      Objects.requireNonNull(command, "command");
      Objects.requireNonNull(node, "node");
      Objects.requireNonNull(command);
      this.register(command, node, command::testPermissionSilent);
   }

   default void register(Command command, LiteralArgumentBuilder<?> argumentBuilder) {
      Objects.requireNonNull(command, "command");
      Objects.requireNonNull(argumentBuilder, "argumentBuilder");
      this.register(command, argumentBuilder.build());
   }

   void register(LiteralCommandNode<?> var1);

   default void register(LiteralArgumentBuilder<?> argumentBuilder) {
      Objects.requireNonNull(argumentBuilder, "argumentBuilder");
      this.register(argumentBuilder.build());
   }
}
