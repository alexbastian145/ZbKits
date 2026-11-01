package dev.viper.utils.sql.query;

import dev.viper.utils.sql.enums.QueryOperation;
import java.util.Objects;

public class SQLUpdateQuery {
   private String tableName;
   private String setColumn;
   private Object setValue;
   private String whereColumn;
   private QueryOperation operation;
   private Object whereValue;

   public static SQLUpdateQuery of() {
      return new SQLUpdateQuery();
   }

   public SQLUpdateQuery table(String tableName) {
      this.tableName = tableName;
      return this;
   }

   public SQLUpdateQuery value(String column, Object value) {
      this.setColumn = column;
      this.setValue = value;
      return this;
   }

   public SQLUpdateQuery where(String column, QueryOperation operation, Object value) {
      this.whereColumn = column;
      this.operation = operation;
      this.whereValue = value;
      return this;
   }

   public String build() {
      StringBuilder query = new StringBuilder("UPDATE ");
      query.append(this.tableName).append(" SET ");
      query.append(this.setColumn).append(" = ");
      if (this.setValue instanceof String) {
         query.append("'").append(this.setValue).append("'");
      } else {
         query.append(this.setValue);
      }

      query.append(" WHERE ").append(this.whereColumn).append(" ");
      if (Objects.requireNonNull(this.operation) == QueryOperation.EQUALS) {
         query.append("=").append(" '").append(this.whereValue).append("'");
      }

      return query.toString();
   }
}
