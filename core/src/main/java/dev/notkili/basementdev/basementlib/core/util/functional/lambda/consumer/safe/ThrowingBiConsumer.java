package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe;

@FunctionalInterface
public interface ThrowingBiConsumer<T1, T2, E extends Exception> {
    void accept(T1 t1, T2 t2) throws E;
}
