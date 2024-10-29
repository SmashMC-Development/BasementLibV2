package dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.QuadConsumer;

/**
 * Represents an operation that accepts four input arguments and returns no result. <br>
 * Unlike {@link QuadConsumer}, this operation can throw a checked exception. <br>
 * @param <T1> The type of the first input to the operation
 * @param <T2> The type of the second input to the operation
 * @param <T3> The type of the third input to the operation
 * @param <T4> The type of the fourth input to the operation
 * @param <E> The type of the exception that can be thrown
 * @author NotKili
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThrowingQuadConsumer<T1, T2, T3, T4, E extends Exception> {
    /**
     * Performs this operation on the given arguments.
     * @param t1 The first input argument
     * @param t2 The second input argument
     * @param t3 The third input argument
     * @param t4 The fourth input argument
     * @throws E The exception that can be thrown
     * @author NotKili
     * @since 1.0.0
     */
    void accept(T1 t1, T2 t2, T3 t3, T4 t4) throws E;
}
