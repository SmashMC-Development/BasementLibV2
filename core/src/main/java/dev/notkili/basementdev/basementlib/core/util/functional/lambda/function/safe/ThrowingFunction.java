package dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.safe;

@FunctionalInterface
public interface ThrowingFunction<T1, R, E extends Exception> {
    R apply(T1 t1) throws E;
}
