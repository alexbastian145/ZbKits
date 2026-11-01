package dev.viper.utils.consumers;

public interface TripleConsumer<A, B, C> {
   void execute(A var1, B var2, C var3);
}
