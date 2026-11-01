package dev.viper.utils.sql.query;

import dev.viper.utils.sql.enums.PrimaryColumn;
import java.util.ArrayList;
import java.util.List;

public class SQLTableBuilder {
   private String tableName;
   private final List<Column> columns = new ArrayList();

   public static SQLTableBuilder of() {
      return new SQLTableBuilder();
   }

   public SQLTableBuilder name(String tableName) {
      this.tableName = tableName;
      return this;
   }

   public SQLTableBuilder column(String name, String type, PrimaryColumn isPrimary) {
      Column column = new Column(name, type, isPrimary);
      this.columns.add(column);
      return this;
   }

   public SQLTableBuilder column(String name, String type) {
      return this.column(name, type, PrimaryColumn.FALSE);
   }

   public SQLTableBuilder column(String name, String type, boolean isPrimary) {
      return isPrimary ? this.column(name, type, PrimaryColumn.TRUE) : this.column(name, type, PrimaryColumn.FALSE);
   }

   public String build() {
      StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
      query.append(this.tableName).append(" (");
      List<String> primaryKeys = new ArrayList();

      for(Column column : this.columns) {
         query.append(column.name()).append(" ").append(column.type());
         if (column.isPrimary() == PrimaryColumn.TRUE) {
            primaryKeys.add(column.name());
         }

         query.append(", ");
      }

      query.setLength(query.length() - 2);
      if (!primaryKeys.isEmpty()) {
         query.append(", PRIMARY KEY(");

         for(String pk : primaryKeys) {
            query.append(pk).append(", ");
         }

         query.setLength(query.length() - 2);
         query.append(")");
      }

      query.append(")");
      return query.toString();
   }

   public static record Column(String name, String type, PrimaryColumn isPrimary) {
   }
}
