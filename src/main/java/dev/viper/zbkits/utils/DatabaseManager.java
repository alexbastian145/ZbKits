package dev.viper.zbkits.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
   private String dbUrl;
   private String dbUser;
   private String dbPassword;
   private Connection connection;

   public DatabaseManager(String dbUrl, String dbUser, String dbPassword) {
      this.dbUrl = dbUrl;
      this.dbUser = dbUser;
      this.dbPassword = dbPassword;
   }

   public void connect() throws SQLException {
      if (this.connection != null && !this.connection.isClosed()) {
         this.connection.close();
      }

      this.connection = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
   }

   public void ensureConnection() throws SQLException {
      if (this.connection == null || this.connection.isClosed()) {
         this.connect();
      }

   }

   public void closeConnection() throws SQLException {
      if (this.connection != null && !this.connection.isClosed()) {
         this.connection.close();
      }

   }

   public Connection getConnection() {
      return this.connection;
   }
}
