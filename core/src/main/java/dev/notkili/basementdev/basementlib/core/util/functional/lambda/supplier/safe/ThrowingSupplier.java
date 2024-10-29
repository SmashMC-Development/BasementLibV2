package dev.notkili.basementdev.basementlib.core.util.functional.lambda.supplier.safe;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
}
