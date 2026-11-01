package dev.viper.utils.resource;

import java.util.Arrays;
import java.util.List;

public class ResourceOptions {
   private List<String> header;
   private List<String> footer;
   private boolean defaults;

   public ResourceOptions(List<String> header, List<String> footer, boolean defaults) {
      this.header = header;
      this.footer = footer;
      this.defaults = defaults;
   }

   public ResourceOptions() {
      this.header = null;
      this.footer = null;
      this.defaults = false;
   }

   public List<String> header() {
      return this.header;
   }

   public List<String> footer() {
      return this.footer;
   }

   public boolean defaults() {
      return this.defaults;
   }

   public ResourceOptions header(List<String> header) {
      this.header = header;
      return this;
   }

   public ResourceOptions footer(List<String> footer) {
      this.footer = footer;
      return this;
   }

   public ResourceOptions header(String... header) {
      return this.header(Arrays.asList(header));
   }

   public ResourceOptions footer(String... footer) {
      return this.footer(Arrays.asList(footer));
   }

   public ResourceOptions defaults(boolean defaults) {
      this.defaults = defaults;
      return this;
   }
}
