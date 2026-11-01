package dev.viper.utils.sql.auth;

import dev.viper.utils.sql.connection.SQLConnector;

public class SQLAuthentication {
   private String host;
   private int port;
   private String database;
   private String username;
   private String password;

   public static SQLAuthentication of() {
      return new SQLAuthentication();
   }

   public SQLAuthentication host(String host) {
      this.host = host;
      return this;
   }

   public SQLAuthentication port(int port) {
      this.port = port;
      return this;
   }

   public SQLAuthentication database(String database) {
      this.database = database;
      return this;
   }

   public SQLAuthentication username(String username) {
      this.username = username;
      return this;
   }

   public SQLAuthentication password(String password) {
      this.password = password;
      return this;
   }

   public SQLConnector build() {
      return new SQLConnector(this);
   }

   public String host() {
      return this.host;
   }

   public int port() {
      return this.port;
   }

   public String database() {
      return this.database;
   }

   public String username() {
      return this.username;
   }

   public String password() {
      return this.password;
   }
}
