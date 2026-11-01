package dev.viper.utils.sql.query;

import java.util.HashMap;
import java.util.Map;

public class SQLInsertQuery {
   private String tableName;
   private final Map<String, Object> values = new HashMap();

   public static SQLInsertQuery of() {
      return new SQLInsertQuery();
   }

   public SQLInsertQuery into(String tableName) {
      this.tableName = tableName;
      return this;
   }

   public SQLInsertQuery value(String column, Object value) {
      this.values.put(column, value);
      return this;
   }

   public String build() {
      StringBuilder query = new StringBuilder("INSERT INTO ");
      query.append(this.tableName).append(" (");

      for(String column : this.values.keySet()) {
         query.append(column).append(", ");
      }

      query.setLength(query.length() - 2);
      query.append(") VALUES (");

      for(Object value : this.values.values()) {
         if (value instanceof String) {
            query.append("'").append(value).append("', ");
         } else {
            query.append(value).append(", ");
         }
      }

      query.setLength(query.length() - 2);
      query.append(")");
      return query.toString();
   }
}
