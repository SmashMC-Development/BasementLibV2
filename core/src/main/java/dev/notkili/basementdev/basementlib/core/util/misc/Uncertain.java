package dev.notkili.basementdev.basementlib.core.util.misc;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe.Function;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.supplier.unsafe.Supplier;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe.ThrowingConsumer;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.safe.ThrowingFunction;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * A container object which may or may not contain a non-{@code null} value.
 * If a value is present, {@link #isPresent()} returns {@code true}. If no
 * value is present, the object is considered <i>empty</i> and
 * {@link #isPresent()} returns {@code false}.
 *
 * <p>Additional methods that depend on the presence or absence of a contained value are provided, such as: <br>
 * - {@link #orElse(Object)} <br>
 * - {@link #orElse(Supplier)} <br>
 * - {@link #filter(Predicate)} <br>
 * - {@link #map(Function)} <br>
 * - {@link #update(Function)} <br>
 * - {@link #ifPresent(ThrowingConsumer)} <br>
 * - {@link #ifPresentOrElse(ThrowingConsumer, Runnable)} <br>
 * - {@link #ifPresentReturn(ThrowingFunction)} <br>
 * - {@link #ifPresentReturnSafe(ThrowingFunction)} <br>
 * - {@link #ifPresentOrElseReturn(ThrowingFunction, Supplier)} <br>
 * - {@link #ifPresentOrElseReturnSafe(ThrowingFunction, Supplier)} <br>
 *
 * @apiNote
 * In comparison to {@link java.util.Optional}, this class is designed to be more flexible and provide more utility methods.
 * Serialization is supported for this class, allowing it to be used in a wider range of applications. <br>
 * The main use case of this is still to be used as a return type for methods where there is a clear need to differentiate between a {@code result} and {@code no result}
 *
 * @param <T> the type of value
 * @author NotKili
 * @since 1.0.0
 */
public class Uncertain<T> {
    private static final Uncertain<?> EMPTY = new Uncertain<>(null);

    private T value;

    private Uncertain() {
        this.value = null;
    }

    private Uncertain(T value) {
        this.value = value;
    }


    /**
     * Creates a new empty {@link Uncertain} of type V
     * @return An EMPTY {@link Uncertain}
     * @param <T> Type
     * @author NotKili
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    public static <T> Uncertain<T> empty() {
        return (Uncertain<T>) EMPTY;
    }

    /**
     * Creates a new {@link Uncertain} of type {@link T} containing the provided value.
     * The value CAN be null, which will result in an empty {@link Uncertain}
     * @param value The value the {@link Uncertain} is supposed to contain
     * @return An {@link Uncertain} containing the provided value. CAN be empty
     * @param <T> Type
     * @author NotKili
     * @since 1.0.0
     */
    public static <T> Uncertain<T> of(T value) {
        return new Uncertain<>(value);
    }

    /**
     * Checks if this {@link Uncertain} contains a value
     * @return {@code true} if this {@link Uncertain} contains a value, {@code false} otherwise
     * @author NotKili
     * @since 1.0.0
     */
    public boolean isPresent() {
        return this.value != null;
    }

    /**
     * Checks if this {@link Uncertain} is empty
     * @return {@code true} if this {@link Uncertain} is empty, {@code false} otherwise
     * @author NotKili
     * @since 1.0.0
     */
    public boolean isMissing() {
        return this.value == null;
    }

    /**
     * Returns the value of the {@link Uncertain} if it is present, otherwise throws a {@link NoSuchElementException}
     * @return The value of the {@link Uncertain}
     * @throws NoSuchElementException If the {@link Uncertain} is empty
     * @author NotKili
     * @since 1.0.0
     */
    public T get() throws NoSuchElementException {
        if (this.value == null) {
            throw new NoSuchElementException("No element defined for 'value'");
        }

        return this.value;
    }

    /**
     * Returns the value of this {@link Uncertain} if it is present, otherwise returns the provided default value
     * @param defaultValue The default value to return if this {@link Uncertain} is empty
     * @return The value of this {@link Uncertain} if it is present, otherwise the provided default value
     * @author NotKili
     * @since 1.0.0
     */
    public T orElse(T defaultValue) {
        if (isMissing()) {
            return defaultValue;
        }

        return this.value;
    }

    /**
     * Returns the value of this {@link Uncertain} if it is present, otherwise invokes the provided {@link Supplier} and returns the result.
     * @param defaultValue The {@link Supplier} to invoke if this {@link Uncertain} is empty
     * @return The value of this {@link Uncertain} if it is present, otherwise the result from invoking the provided {@link Supplier}
     * @author NotKili
     * @since 1.0.0
     */
    public T orElse(Supplier<T> defaultValue) {
        if (isMissing()) {
            return defaultValue.get();
        }

        return this.value;
    }

    /**
     * Returns the value of this {@link Uncertain} without checking if it is present or not. <br>
     * <b>Important: </b>The return value can be {@code null}
     * @return The value of this {@link Uncertain}, which can be null
     * @author NotKili
     * @since 1.0.0
     */
    @Nullable
    public T unsafe() {
        return this.value;
    }

    /**
     * Updates the value of this {@link Uncertain} if it is present, otherwise does nothing
     * @param modifier The {@link Function} to modify the value with
     * @return The same {@link Uncertain}
     * @author NotKili
     * @since 1.0.0
     */
    public Uncertain<T> update(Function<T, T> modifier) {
        if (isMissing()) {
            return this;
        }

        this.value = modifier.apply(value);
        return this;
    }

    /**
     * Maps the value of this {@link Uncertain} to a new value of type {@link V} if it is present, otherwise returns an empty {@link Uncertain}
     * @param mapper The {@link Function} to map the value with
     * @return A new {@link Uncertain} containing the mapped value, or an empty {@link Uncertain}
     * @param <V> The new type
     * @author NotKili
     * @since 1.0.0
     */
    public <V> Uncertain<V> map(Function<T, V> mapper) {
        if (isMissing()) {
            return Uncertain.empty();
        }

        return Uncertain.of(mapper.apply(this.value));
    }

    /**
     * Matches the value of this {@link Uncertain} to a {@link Predicate} filter, returning the same {@link Uncertain} if the filter matches, otherwise an empty {@link Uncertain}
     * @param filter The {@link Predicate} to match the value against
     * @return The same {@link Uncertain} if the filter matches, otherwise an empty {@link Uncertain}
     * @author NotKili
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    public Uncertain<T> filter(Predicate<T> filter) {
        if (isPresent()) {
            if (filter.test(this.value)) {
                return this;
            }
        }

        return (Uncertain<T>) Uncertain.empty();
    }

    /**
     * Executes the provided {@link ThrowingConsumer} if the {@link Uncertain} is present, otherwise does nothing
     * @param task The {@link ThrowingConsumer} to execute if the {@link Uncertain} is present
     * @param <E> The exception type which can be thrown by the {@link ThrowingConsumer}
     * @throws E The exception thrown by the {@link ThrowingConsumer}
     * @author NotKili
     * @since 1.0.0
     */
    public <E extends Exception> void ifPresent(ThrowingConsumer<T, E> task) throws E {
        if (isPresent()) {
            task.accept(this.value);
        }
    }

    /**
     * Executes the provided {@link ThrowingConsumer} if the {@link Uncertain} is present, otherwise executes the provided {@link Runnable}
     * @param task The {@link ThrowingConsumer} to execute if the {@link Uncertain} is present
     * @param runnable The {@link Runnable} to execute if the {@link Uncertain} is empty
     * @param <E> The exception type which can be thrown by the {@link ThrowingConsumer}
     * @throws E The exception thrown by the {@link ThrowingConsumer}
     * @author NotKili
     * @since 1.0.0
     */
    public <E extends Exception> void ifPresentOrElse(ThrowingConsumer<T, E> task, Runnable runnable) throws E {
        if (isPresent()) {
            task.accept(this.value);
        }  else {
            runnable.run();
        }
    }

    /**
     * Executes the provided {@link ThrowingFunction} if the {@link Uncertain} is present, returning the result. Returns null if empty.
     * @param task The {@link ThrowingFunction} task to execute if the {@link Uncertain} is present
     * @param <R> The return type of the {@link ThrowingFunction}
     * @param <E> The exception type which can be thrown by the {@link ThrowingFunction}
     * @return The result of the {@link ThrowingFunction} or null if {@link Uncertain} is empty
     * @throws E The exception thrown by the {@link ThrowingFunction}
     * @author NotKili
     * @since 1.0.0
     */
    @Nullable
    public <R, E extends Exception> R ifPresentReturn(ThrowingFunction<T, R, E> task) throws E {
        if (isPresent()) {
            return task.apply(this.value);
        } else {
            return null;
        }
    }

    /**
     * Executes the provided {@link ThrowingFunction} if the {@link Uncertain} is present, returning the result wrapped in a new {@link Uncertain}. 
     * Returns an empty {@link Uncertain} if absent.
     * @param task The {@link ThrowingFunction} to execute if the {@link Uncertain} is present
     * @param <R> The return type of the {@link ThrowingFunction}
     * @param <E> The exception type which can be thrown by the {@link ThrowingFunction}
     * @return The result of the {@link ThrowingFunction} wrapped in {@link Uncertain} or an empty {@link Uncertain} if absent
     * @throws E The exception thrown by the {@link ThrowingFunction}
     * @author NotKili
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    public <R, E extends Exception> Uncertain<R> ifPresentReturnSafe(ThrowingFunction<T, R, E> task) throws E {
        if (isPresent()) {
            return Uncertain.of(task.apply(this.value));
        } else {
            return (Uncertain<R>) Uncertain.empty();
        }
    }

    /**
     * Executes the provided {@link ThrowingFunction} if the {@link Uncertain} is present, returning the result.
     * If absent, invokes the provided {@link Supplier} and returns the result.
     * @param task The {@link ThrowingFunction} to execute if the {@link Uncertain} is present
     * @param defaultValue The fallback {@link Supplier} if {@link Uncertain} is empty
     * @param <R> The return type of the {@link ThrowingFunction}
     * @param <E> The exception type which can be thrown by the {@link ThrowingFunction}
     * @return The result of the {@link ThrowingFunction} or the result of invoking the {@link Supplier} if {@link this} is empty
     * @throws E The exception thrown by the {@link ThrowingFunction}
     * @author NotKili
     * @since 1.0.0
     */
    public <R, E extends Exception> R ifPresentOrElseReturn(ThrowingFunction<T, R, E> task, Supplier<R> defaultValue) throws E {
        if (isPresent()) {
            return task.apply(this.value);
        } else {
            return defaultValue.get();
        }
    }

    /**
     * Executes the provided {@link ThrowingFunction} if the {@link Uncertain} is present, returning the result wrapped in {@link Uncertain}.
     * If absent, invokes the provided {@link Supplier} and returns the result wrapped in {@link Uncertain}.
     * @param task The {@link ThrowingFunction} to execute if the {@link Uncertain} is present
     * @param defaultValue The fallback {@link Supplier} to use if {@link Uncertain} is empty
     * @param <R> The return type of the {@link ThrowingFunction}
     * @param <E> The exception type which can be thrown by the {@link ThrowingFunction}
     * @return The result of the {@link ThrowingFunction} or the result of {@link Supplier}, both wrapped in a {@link Uncertain} if absent
     * @throws E The exception thrown by the {@link ThrowingFunction}
     * @author NotKili
     * @since 1.0.0
     */
    public <R, E extends Exception> Uncertain<R> ifPresentOrElseReturnSafe(ThrowingFunction<T, R, E> task, Supplier<R> defaultValue) throws E {
        if (isPresent()) {
            return Uncertain.of(task.apply(this.value));
        } else {
            return Uncertain.of(defaultValue.get());
        }
    }
}
