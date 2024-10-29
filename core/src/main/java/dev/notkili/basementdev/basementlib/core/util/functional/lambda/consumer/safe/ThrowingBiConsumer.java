package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.BiConsumer;

/**
 * Represents an operation that accepts two input arguments and returns no result. <br>
 * Unlike {@link BiConsumer}, this operation can throw a checked exception. <br>
 * @param <T1> The type of the first input to the operation
 * @param <T2> The type of the second input to the operation
 * @param <E> The type of the exception that can be thrown
 * @author NotKili 
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T1, T2, E extends Exception> {
    /**
     * Performs this operation on the given arguments.
     * @param t1 The first input argument
     * @param t2 The second input argument
     * @throws E The exception that can be thrown
     * @author NotKili
     */
    void accept(T1 t1, T2 t2) throws E;
}
