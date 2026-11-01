package dev.viper.utils.sql.query;

import dev.viper.utils.sql.enums.QueryOperation;
import java.util.Objects;

public class SQLDeleteQuery {
   private String tableName;
   private String whereColumn;
   private QueryOperation operation;
   private Object value;

   public static SQLDeleteQuery of() {
      return new SQLDeleteQuery();
   }

   public SQLDeleteQuery from(String tableName) {
      this.tableName = tableName;
      return this;
   }

   public SQLDeleteQuery where(String column, QueryOperation operation, Object value) {
      this.whereColumn = column;
      this.operation = operation;
      this.value = value;
      return this;
   }

   public String build() {
      StringBuilder query = new StringBuilder("DELETE FROM ");
      query.append(this.tableName).append(" WHERE ").append(this.whereColumn).append(" ");
      if (Objects.requireNonNull(this.operation) == QueryOperation.EQUALS) {
         query.append("=").append(" '").append(this.value).append("'");
      }

      return query.toString();
   }
}
