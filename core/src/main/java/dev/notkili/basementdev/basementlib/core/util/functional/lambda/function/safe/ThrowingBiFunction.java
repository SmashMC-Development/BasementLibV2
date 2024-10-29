package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.safe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a result. <br>
 * Unlike {@link BiFunction}, this function can throw a checked exception.
 * @param <T1> The type of the first argument to the function
 * @param <T2> The type of the second argument to the function
 * @param <R> The type of the result of the function
 * @param <E> The type of the exception that can be thrown
 * @author NotKili
 */
@FunctionalInterface
public interface ThrowingBiFunction<T1, T2, R, E extends Exception> {
    /**
     * Applies this function to the given arguments.
     * @param t1 The first function argument
     * @param t2 The second function argument
     * @return The function result
     * @throws E The exception that can be thrown
     * @author NotKili
     */
    R apply(T1 t1, T2 t2) throws E;
}
