package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe;

@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {
    R apply(T1 t1, T2 t2, T3 t3);
}
