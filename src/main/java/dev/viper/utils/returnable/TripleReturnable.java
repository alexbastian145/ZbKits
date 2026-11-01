package dev.viper.utils.returnable;

public interface TripleReturnable<R, A, B> {
   R returnVal(A var1, B var2);
}
