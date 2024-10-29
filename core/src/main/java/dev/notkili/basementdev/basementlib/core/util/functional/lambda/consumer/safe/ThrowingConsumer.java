package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.Consumer;

/**
 * Represents an operation that accepts one input argument and returns no result. <br>
 * Unlike {@link Consumer}, this operation can throw a checked exception. <br>
 * @param <T1> The type of the input to the operation
 * @param <E> The type of the exception that can be thrown
 * @author NotKili
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThrowingConsumer<T1, E extends Exception> {
    /**
     * Performs this operation on the given arguments.
     * @param t The input argument
     * @throws E The exception that can be thrown
     * @author NotKili
     * @since 1.0.0
     */
    void accept(T1 t) throws E;
}
