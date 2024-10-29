package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe.ThrowingConsumer;

/**
 * Represents an operation that accepts one input argument and returns no result. <br>
 * Unlike {@link ThrowingConsumer}, this operation can not throw a checked exception.
 * @param <T1> The type of the input to the operation
 * @author NotKili
 */
@FunctionalInterface
public interface Consumer<T1> {
    /**
     * Performs this operation on the given arguments.
     * @param t1 The input argument
     * @author NotKili
     */
    void accept(T1 t1);
}
