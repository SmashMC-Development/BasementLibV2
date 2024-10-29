package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe;

@FunctionalInterface
public interface ThrowingConsumer<T1, E extends Exception> {
    void accept(T1 t) throws E;
}
