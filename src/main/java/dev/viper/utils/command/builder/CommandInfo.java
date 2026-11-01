package dev.viper.utils.command.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.bukkit.command.CommandSender;

public class CommandInfo {
   private String usage;
   private List<String> aliases = new ArrayList();
   private String description;
   private String permission;
   private String namespace;
   private String permissionMessage;
   private BiConsumer<CommandSender, String> errorSender;

   public String getUsage() {
      return this.usage;
   }

   public void setUsage(String usage) {
      this.usage = usage;
   }

   public List<String> getAliases() {
      return this.aliases;
   }

   public void setAliases(List<String> aliases) {
      this.aliases = aliases;
   }

   public void addAlias(String alias) {
      this.aliases.add(alias);
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getPermission() {
      return this.permission;
   }

   public void setPermission(String permission) {
      this.permission = permission;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public void setNamespace(String namespace) {
      this.namespace = namespace;
   }

   public String getPermissionMessage() {
      return this.permissionMessage;
   }

   public void setPermissionMessage(String permissionMessage) {
      this.permissionMessage = permissionMessage;
   }

   public BiConsumer<CommandSender, String> getErrorSender() {
      return this.errorSender;
   }

   public void setErrorSender(BiConsumer<CommandSender, String> errorSender) {
      this.errorSender = errorSender;
   }
}
