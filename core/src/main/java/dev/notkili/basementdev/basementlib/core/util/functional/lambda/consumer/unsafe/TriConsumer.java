package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe.ThrowingTriConsumer;

/**
 * Represents an operation that accepts three input arguments and returns no result. <br>
 * Unlike {@link ThrowingTriConsumer}, this operation can not throw a checked exception.
 * @param <T1> The type of the first input to the operation
 * @param <T2> The type of the second input to the operation
 * @param <T3> The type of the third input to the operation
 * @author NotKili
 */
@FunctionalInterface
public interface TriConsumer<T1, T2, T3> {
    /**
     * Performs this operation on the given arguments.
     * @param t1 The first input argument
     * @param t2 The second input argument
     * @param t3 The third input argument
     * @author NotKili
     */
    void accept(T1 t1, T2 t2, T3 t3);
}
