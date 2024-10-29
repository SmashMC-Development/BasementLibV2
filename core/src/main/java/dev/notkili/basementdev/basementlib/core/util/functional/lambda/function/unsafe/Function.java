package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe;

/**
 * Represents a function that accepts one argument and produces a result. <br>
 * Unlike {@link Function}, this function can not throw a checked exception.
 * @param <T1> The type of the first argument to the function
 * @param <R> The type of the result of the function
 * @author NotKili
 * @since 1.0.0
 */
@FunctionalInterface
public interface Function<T1, R> {
    /**
     * Applies this function to the given argument.
     * @param t1 The function argument
     * @return The function result
     * @author NotKili
     * @since 1.0.0
     */
    R apply(T1 t1);
}
