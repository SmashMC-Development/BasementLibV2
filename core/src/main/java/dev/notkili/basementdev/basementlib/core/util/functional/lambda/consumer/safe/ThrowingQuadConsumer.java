package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe;

@FunctionalInterface
public interface ThrowingQuadConsumer<T1, T2, T3, T4, E extends Exception> {
    void accept(T1 t1, T2 t2, T3 t3, T4 t4) throws E;
}
