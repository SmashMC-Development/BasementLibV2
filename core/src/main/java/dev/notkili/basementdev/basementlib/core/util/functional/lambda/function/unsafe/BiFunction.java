package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe;

@FunctionalInterface
public interface BiFunction<T1, T2, R> {
    R apply(T1 t1, T2 t2);
}
