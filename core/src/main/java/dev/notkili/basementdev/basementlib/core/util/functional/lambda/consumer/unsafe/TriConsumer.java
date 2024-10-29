package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe;

@FunctionalInterface
public interface TriConsumer<T1, T2, T3> {
    void accept(T1 t1, T2 t2, T3 t3);
}
