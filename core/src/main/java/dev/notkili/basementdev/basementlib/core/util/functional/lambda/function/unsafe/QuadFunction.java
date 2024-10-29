package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe;

/**
 * Represents a function that accepts four arguments and produces a result. <br>
 * Unlike {@link QuadFunction}, this function can not throw a checked exception.
 * @param <T1> The type of the first argument to the function
 * @param <T2> The type of the second argument to the function
 * @param <T3> The type of the third argument to the function
 * @param <T4> The type of the fourth argument to the function
 * @param <R> The type of the result of the function
 * @author NotKili
 */
@FunctionalInterface
public interface QuadFunction<T1, T2, T3, T4, R> {
    /**
     * Applies this function to the given arguments.
     * @param t1 The first function argument
     * @param t2 The second function argument
     * @param t3 The third function argument
     * @param t4 The fourth function argument
     * @return The function result
     * @author NotKili
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4);
}
