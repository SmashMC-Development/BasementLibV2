package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.safe;

@FunctionalInterface
public interface ThrowingQuadFunction<T1, T2, T3, T4, R, E extends Exception> {
    R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws E;
}
