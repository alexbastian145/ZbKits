package dev.viper.utils.sql.connection;

import dev.viper.utils.sql.auth.SQLAuthentication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public record SQLConnector(SQLAuthentication authentication) {
   public static SQLConnector of() {
      SQLAuthentication authentication = SQLAuthentication.of().database("database").host("localhost").password("").username("root").port(3306);
      return new SQLConnector(authentication);
   }

   public static SQLConnector of(SQLAuthentication authentication) {
      return new SQLConnector(authentication);
   }

   public Connection connect() {
      String var10000 = this.authentication.host();
      String url = "jdbc:mysql://" + var10000 + ":" + this.authentication.port() + "/" + this.authentication.database();

      try {
         return DriverManager.getConnection(url, this.authentication.username(), this.authentication.password());
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }
}
