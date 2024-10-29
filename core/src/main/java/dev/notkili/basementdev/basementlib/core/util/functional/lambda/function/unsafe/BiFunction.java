package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.safe.ThrowingBiFunction;

/**
 * Represents a function that accepts two arguments and produces a result. <br>
 * Unlike {@link ThrowingBiFunction}, this function can not throw a checked exception.
 * @param <T1> The type of the first argument to the function
 * @param <T2> The type of the second argument to the function
 * @param <R> The type of the result of the function
 * @author NotKili
 */
@FunctionalInterface
public interface BiFunction<T1, T2, R> {
    /**
     * Applies this function to the given arguments.
     * @param t1 The first function argument
     * @param t2 The second function argument
     * @return The function result
     * @author NotKili
     */
    R apply(T1 t1, T2 t2);
}
