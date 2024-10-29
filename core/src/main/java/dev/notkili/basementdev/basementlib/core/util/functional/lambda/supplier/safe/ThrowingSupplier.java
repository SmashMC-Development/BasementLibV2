package dev.notkili.basementdev.basementlib.core.util.functional.lambda.supplier.safe;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.supplier.unsafe.Supplier;

/**
 * Represents a supplier of results. <br>
 * Unlike {@link Supplier}, this function can throw a checked exception.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * @param <T> the type of results supplied by this supplier
 * @param <E> the type of exception that can be thrown
 * @author NotKili
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    /**
     * Computes a result.
     * @return The result of the computation
     * @throws E The exception that can be thrown while computing the result
     * @author NotKili
     * @since 1.0.0
     */
    T get() throws E;
}
