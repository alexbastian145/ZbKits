package dev.viper.utils.command.wrapper;

import dev.viper.utils.command.builder.Argument;
import dev.viper.utils.command.builder.CommandContext;
import dev.viper.utils.command.builder.CommandInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleCommand extends Command {
   private final CommandInfo info = new CommandInfo();
   private Consumer<CommandContext> executor;
   private Function<CommandContext, List<String>> tabCompleter;
   private final List<Argument<?>> arguments = new ArrayList();
   private final Map<String, SimpleSubCommand> subcommands = new HashMap();
   private final Map<Integer, List<TabCommand>> tabComplete = new HashMap();
   private static CommandMap commandMap;

   public SimpleCommand(String name, Consumer<CommandInfo> infoConsumer) {
      super(name);
      infoConsumer.accept(this.info);
      this.setDescription(this.info.getDescription());
      this.setUsage(this.info.getUsage());
      this.setAliases(this.info.getAliases());
      this.setPermission(this.info.getPermission());
      this.setPermissionMessage(this.info.getPermissionMessage());
   }

   public SimpleCommand executes(Consumer<CommandContext> executor) {
      this.executor = executor;
      return this;
   }

   public SimpleCommand suggests(Function<CommandContext, List<String>> tabCompleter) {
      this.tabCompleter = tabCompleter;
      return this;
   }

   public SimpleCommand withArgument(Argument<?> argument) {
      this.arguments.add(argument);
      return this;
   }

   public SimpleCommand withSubCommand(SimpleSubCommand subCommand) {
      this.subcommands.put(subCommand.getName(), subCommand);
      return this;
   }

   public SimpleCommand withTabComplete(int index, List<TabCommand> commands) {
      this.tabComplete.put(index, commands);
      return this;
   }

   public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
      CommandContext ctx = new CommandContext(sender, label, args);
      if (args.length > 0 && this.subcommands.containsKey(args[0])) {
         ((SimpleSubCommand)this.subcommands.get(args[0])).execute(ctx);
      } else if (this.executor != null) {
         this.executor.accept(ctx);
      } else {
         sender.sendMessage("Command not implemented.");
      }

      return true;
   }

   public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
      CommandContext ctx = new CommandContext(sender, alias, args);
      if (args.length > 0 && this.subcommands.containsKey(args[0])) {
         return ((SimpleSubCommand)this.subcommands.get(args[0])).suggestions(ctx);
      } else if (this.tabCompleter != null) {
         return (List)this.tabCompleter.apply(ctx);
      } else {
         int index = args.length - 1;
         if ((this.getPermission() == null || sender.hasPermission(this.getPermission())) && !this.tabComplete.isEmpty() && this.tabComplete.containsKey(index)) {
            List<String> suggestions = (List)((List)this.tabComplete.get(index)).stream().filter((tabCommand) -> tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[index - 1])).filter((tabCommand) -> tabCommand.getPermission() == null || sender.hasPermission(tabCommand.getPermission())).filter((tabCommand) -> tabCommand.getText().startsWith(args[index])).map(TabCommand::getText).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
            return suggestions.isEmpty() ? super.tabComplete(sender, alias, args) : suggestions;
         } else {
            return super.tabComplete(sender, alias, args);
         }
      }
   }

   public void register(JavaPlugin plugin) {
      commandMap.register(plugin.getName(), this);
   }

   public abstract void execute(CommandContext var1);

   public abstract List<String> suggestions(CommandContext var1);

   static {
      try {
         commandMap = Bukkit.getCommandMap();
      } catch (Throwable e) {
         e.printStackTrace();
      }

   }

   public static class TabCommand {
      private final String text;
      private final String textAvant;
      private final String permission;

      public TabCommand(String text, String textAvant, String permission) {
         this.text = text;
         this.textAvant = textAvant;
         this.permission = permission;
      }

      public String getText() {
         return this.text;
      }

      public String getTextAvant() {
         return this.textAvant;
      }

      public String getPermission() {
         return this.permission;
      }
   }
}
