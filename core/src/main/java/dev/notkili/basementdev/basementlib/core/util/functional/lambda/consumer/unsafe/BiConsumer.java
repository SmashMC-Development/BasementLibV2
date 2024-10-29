package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe.ThrowingBiConsumer;

/**
 * Represents an operation that accepts two input arguments and returns no result. <br>
 * Unlike {@link ThrowingBiConsumer}, this operation can not throw a checked exception.
 * @param <T1> The type of the first input to the operation
 * @param <T2> The type of the second input to the operation
 * @author NotKili
 * @since 1.0.0
 */
@FunctionalInterface
public interface BiConsumer<T1, T2> {
    /**
     * Performs this operation on the given arguments.
     * @param t1 The first input argument
     * @param t2 The second input argument
     * @author NotKili
     * @since 1.0.0
     */
    void accept(T1 t1, T2 t2);
}
