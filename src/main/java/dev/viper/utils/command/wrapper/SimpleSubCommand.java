package dev.viper.utils.command.wrapper;

import dev.viper.utils.command.builder.Argument;
import dev.viper.utils.command.builder.CommandContext;
import dev.viper.utils.command.builder.CommandInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SimpleSubCommand {
   private final String name;
   private final CommandInfo info = new CommandInfo();
   private Consumer<CommandContext> executor;
   private Function<CommandContext, List<String>> tabCompleter;
   private final List<Argument<?>> arguments = new ArrayList();

   public SimpleSubCommand(String name, Consumer<CommandInfo> infoConsumer) {
      this.name = name;
      infoConsumer.accept(this.info);
   }

   public SimpleSubCommand executes(Consumer<CommandContext> executor) {
      this.executor = executor;
      return this;
   }

   public SimpleSubCommand suggests(Function<CommandContext, List<String>> tabCompleter) {
      this.tabCompleter = tabCompleter;
      return this;
   }

   public SimpleSubCommand withArgument(Argument<?> argument) {
      this.arguments.add(argument);
      return this;
   }

   public String getName() {
      return this.name;
   }

   public CommandInfo getInfo() {
      return this.info;
   }

   public Consumer<CommandContext> getExecutor() {
      return this.executor;
   }

   public Function<CommandContext, List<String>> getTabCompleter() {
      return this.tabCompleter;
   }

   public List<Argument<?>> getArguments() {
      return this.arguments;
   }

   public abstract void execute(CommandContext var1);

   public abstract List<String> suggestions(CommandContext var1);
}
