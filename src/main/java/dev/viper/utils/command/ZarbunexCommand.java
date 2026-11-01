package dev.viper.utils.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.java.JavaPlugin;

/** @deprecated */
@Deprecated
public abstract class ZarbunexCommand<T extends JavaPlugin> extends Command implements CommandExecutor, PluginIdentifiableCommand {
   private static CommandMap commandMap;
   private final T plugin;
   private final HashMap<Integer, ArrayList<TabCommand>> tabComplete;
   private final String namespace;
   private boolean register = false;
   private final Map<String, ZarbunexCommand<?>> subcommands;

   protected ZarbunexCommand(T plugin, String namespace, String name) {
      super(name);

      assert commandMap != null;

      assert plugin != null;

      assert name.length() > 0;

      this.setLabel(name);
      this.plugin = plugin;
      this.namespace = namespace;
      this.tabComplete = new HashMap();
      this.subcommands = new HashMap();
   }

   protected void setAliases(String... aliases) {
      if (aliases != null && (this.register || aliases.length > 0)) {
         this.setAliases((List)Arrays.stream(aliases).collect(Collectors.toList()));
      }

   }

   protected void addTabbComplete(int indice, String permission, String[] beforeText, String... arg) {
      if (arg != null && arg.length > 0 && indice >= 0) {
         if (this.tabComplete.containsKey(indice)) {
            ((ArrayList)this.tabComplete.get(indice)).addAll((Collection)Arrays.stream(arg).collect(ArrayList::new, (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)), ArrayList::addAll));
         } else {
            this.tabComplete.put(indice, (ArrayList)Arrays.stream(arg).collect(ArrayList::new, (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)), ArrayList::addAll));
         }
      }

   }

   protected void addTabbComplete(int indice, String... arg) {
      this.addTabbComplete(indice, (String)null, (String[])null, arg);
   }

   protected boolean registerCommand() {
      if (!this.register) {
         this.register = commandMap.register(this.namespace, this);
      }

      return this.register;
   }

   public void addSubcommand(ZarbunexCommand<?> subcommand) {
      this.subcommands.put(subcommand.getName(), subcommand);
   }

   public void removeSubcommand(String name) {
      this.subcommands.remove(name);
   }

   public T getPlugin() {
      return this.plugin;
   }

   public boolean execute(CommandSender sender, String command, String[] arg) {
      if (arg.length > 0 && this.subcommands.containsKey(arg[0])) {
         ZarbunexCommand<?> subcommand = (ZarbunexCommand)this.subcommands.get(arg[0]);
         return subcommand.execute(sender, command, (String[])Arrays.copyOfRange(arg, 1, arg.length));
      } else if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
         String var5 = String.valueOf(ChatColor.RED);
         sender.sendMessage(var5 + (this.getPermissionMessage() != null ? this.getPermissionMessage() : "You don't have permission to execute this command!"));
         return false;
      } else if (this.onCommand(sender, this, command, arg)) {
         return true;
      } else {
         String var10001 = String.valueOf(ChatColor.RED);
         sender.sendMessage(var10001 + this.getUsage());
         return false;
      }
   }

   public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
      int indice = args.length - 1;
      if ((this.getPermission() == null || sender.hasPermission(this.getPermission())) && this.tabComplete.size() != 0 && this.tabComplete.containsKey(indice)) {
         List<String> list = (List)((ArrayList)this.tabComplete.get(indice)).stream().filter((tabCommand) -> tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[indice - 1])).filter((tabCommand) -> tabCommand.getPermission() == null || sender.hasPermission(tabCommand.getPermission())).filter((tabCommand) -> tabCommand.getText().startsWith(args[indice])).map(TabCommand::getText).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
         return list.size() < 1 ? super.tabComplete(sender, alias, args) : list;
      } else {
         return super.tabComplete(sender, alias, args);
      }
   }

   static {
      try {
         commandMap = Bukkit.getCommandMap();
      } catch (Throwable e) {
         e.printStackTrace();
      }

   }

   private static class TabCommand {
      private final int indice;
      private final String text;
      private final String permission;
      private final ArrayList<String> textAvant;

      private TabCommand(int indice, String text, String permission, String... textAvant) {
         this.indice = indice;
         this.text = text;
         this.permission = permission;
         if (textAvant != null && textAvant.length >= 1) {
            this.textAvant = (ArrayList)Arrays.stream(textAvant).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
         } else {
            this.textAvant = null;
         }

      }

      public String getText() {
         return this.text;
      }

      public int getIndice() {
         return this.indice;
      }

      public String getPermission() {
         return this.permission;
      }

      public ArrayList<String> getTextAvant() {
         return this.textAvant;
      }
   }
}
