package dev.notkili.basementdev.basementlib.core.util.functional.lambda.supplier.unsafe;

/**
 * Represents a supplier of results. <br>
 * Unlike {@link Supplier}, this function can not throw a checked exception.
 * 
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * @param <T> the type of results supplied by this supplier
 * @author NotKili
 * @since 1.0.0
 */
@FunctionalInterface
public interface Supplier<T> {
    /**
     * Computes a result.
     * @return The result of the computation
     * @author NotKili
     * @since 1.0.0
     */
    T get();
}
