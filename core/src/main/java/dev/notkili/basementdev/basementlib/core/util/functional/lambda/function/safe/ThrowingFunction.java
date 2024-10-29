package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.safe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe.Function;

/**
 * Represents a function that accepts one argument and produces a result. <br>
 * Unlike {@link Function}, this function can throw a checked exception.
 * @param <T1> The type of the first argument to the function
 * @param <R> The type of the result of the function
 * @param <E> The type of the exception that can be thrown
 * @author NotKili
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThrowingFunction<T1, R, E extends Exception> {
    /**
     * Applies this function to the given argument.
     * @param t1 The function argument
     * @return The function result
     * @throws E The exception that can be thrown
     * @author NotKili
     * @since 1.0.0
     */
    R apply(T1 t1) throws E;
}
