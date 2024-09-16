package dev.notkili.basementdev.basementlib.core.util.misc;

import dev.notkili.basementdev.basementlib.core.util.functional.ThrowingConsumer;
import dev.notkili.basementdev.basementlib.core.util.functional.ThrowingReturningConsumer;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
     */
    public static <T> Uncertain<T> of(T value) {
        return new Uncertain<>(value);
    }

    /**
     * Checks if the {@link Uncertain} contains a value
     * @return TRUE if the {@link Uncertain} contains a value, FALSE otherwise
     */
    public boolean isPresent() {
        return this.value != null;
    }

    /**
     * Checks if the {@link Uncertain} is empty
     * @return TRUE if the {@link Uncertain} is empty, FALSE otherwise
     */
    public boolean isMissing() {
        return this.value == null;
    }

    /**
     * Returns the value of the {@link Uncertain} if it is present, otherwise throws a {@link NoSuchElementException}
     * @return The value of the {@link Uncertain}
     * @throws NoSuchElementException If the {@link Uncertain} is empty
     */
    public T get() throws NoSuchElementException {
        if (this.value == null) {
            throw new NoSuchElementException("No element defined for 'value'");
        }

        return this.value;
    }

    /**
     * Returns the value of the {@link Uncertain} if it is present, otherwise returns the provided default value
     * @param defaultValue The default value to return if the {@link Uncertain} is empty
     * @return The value of the {@link Uncertain} if it is present, otherwise the provided default value
     */
    public T orElse(T defaultValue) {
        if (isMissing()) {
            return defaultValue;
        }

        return this.value;
    }

    /**
     * Returns the value of the {@link Uncertain} if it is present, otherwise returns the value returned from the provided {@link Supplier}
     * @param defaultValue The {@link Supplier} to get the default value from if the {@link Uncertain} is empty
     * @return The value of the {@link Uncertain} if it is present, otherwise the value returned from the provided {@link Supplier}
     */
    public T orElse(Supplier<T> defaultValue) {
        if (isMissing()) {
            return defaultValue.get();
        }

        return this.value;
    }

    /**
     * Returns the value of the {@link Uncertain}, which can be null in case the {@link Uncertain} is empty
     * @return The value of the {@link Uncertain}, which can be null
     */
    @Nullable
    public T unsafe() {
        return this.value;
    }

    /**
     * Updates the value of the {@link Uncertain} if it is present, otherwise does nothing
     * @param modifier The function to modify the value with
     * @return The updated {@link Uncertain}
     */
    public Uncertain<T> update(Function<T, T> modifier) {
        if (isMissing()) {
            return this;
        }

        this.value = modifier.apply(value);
        return this;
    }

    /**
     * Maps the value of the {@link Uncertain} to a new value of type {@link V} if it is present, otherwise returns an empty {@link Uncertain}
     * @param mapper The function to map the value with
     * @return A new {@link Uncertain} containing the mapped value, or an empty {@link Uncertain}
     * @param <V> The new type
     */
    public <V> Uncertain<V> map(Function<T, V> mapper) {
        if (isMissing()) {
            return Uncertain.empty();
        }

        return Uncertain.of(mapper.apply(this.value));
    }

    /**
     * Matches the value of the {@link Uncertain} to a {@link Predicate} filter, returning the {@link Uncertain} if the filter matches, otherwise an empty {@link Uncertain}
     * @param filter The filter to match the value against
     * @return The {@link Uncertain} if the filter matches, otherwise an empty {@link Uncertain}
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
     * Executes the provided {@link ThrowingConsumer} task if the {@link Uncertain} is present, otherwise does nothing
     * @param task The {@link ThrowingConsumer} task to execute if the {@link Uncertain} is present
     * @param <E> The exception type which can be thrown by the task
     * @throws E The exception thrown by the task
     */
    public <E extends Exception> void ifPresent(ThrowingConsumer<T, E> task) throws E {
        if (isPresent()) {
            task.accept(this.value);
        }
    }

    /**
     * Executes the provided {@link ThrowingConsumer} task if the {@link Uncertain} is present, otherwise executes the provided {@link Runnable} task
     * @param task The {@link ThrowingConsumer} task to execute if the {@link Uncertain} is present
     * @param runnable The {@link Runnable} task to execute if the {@link Uncertain} is empty
     * @param <E> The exception type which can be thrown by the task
     * @throws E The exception thrown by the task
     * @author NotKili
     */
    public <E extends Exception> void ifPresentOrElse(ThrowingConsumer<T, E> task, Runnable runnable) throws E {
        if (isPresent()) {
            task.accept(this.value);
        }  else {
            runnable.run();
        }
    }

    /**
     * Executes the provided {@link ThrowingReturningConsumer} task if the {@link Uncertain} is present, returning the result. Returns null if empty.
     * @param task The {@link ThrowingReturningConsumer} task to execute if the {@link Uncertain} is present
     * @param <D> The return type of the task
     * @param <E> The exception type which can be thrown by the task
     * @return The result of the task or null if {@link Uncertain} is empty
     * @throws E The exception thrown by the task
     * @author NotKili
     */
    @Nullable
    public <D, E extends Exception> D ifPresentReturn(ThrowingReturningConsumer<D, T, E> task) throws E {
        if (isPresent()) {
            return task.accept(this.value);
        } else {
            return null;
        }
    }

    /**
     * Executes the provided {@link ThrowingReturningConsumer} task if the {@link Uncertain} is present, returning the result wrapped in a new {@link Uncertain}. 
     * Returns an empty {@link Uncertain} if absent.
     * @param task The {@link ThrowingReturningConsumer} task to execute if the {@link Uncertain} is present
     * @param <D> The return type of the {@link ThrowingReturningConsumer} task
     * @param <E> The exception type which can be thrown by the {@link ThrowingReturningConsumer} task
     * @return The result of the {@link ThrowingReturningConsumer} task wrapped in {@link Uncertain} or an empty {@link Uncertain} if absent
     * @throws E The exception thrown by the {@link ThrowingReturningConsumer} task
     * @author NotKili
     */
    @SuppressWarnings("all")
    public <D, E extends Exception> Uncertain<D> ifPresentReturnOpt(ThrowingReturningConsumer<D, T, E> task) throws E {
        if (isPresent()) {
            return Uncertain.of(task.accept(this.value));
        } else {
            return (Uncertain<D>) Uncertain.empty();
        }
    }

    /**
     * Executes the provided {@link ThrowingReturningConsumer} task if the {@link Uncertain} is present, returning the result.
     * If absent, returns the provided {@link Supplier} default value.
     * @param task The {@link ThrowingReturningConsumer} task to execute if the {@link Uncertain} is present
     * @param defaultValue The fallback {@link Supplier} if {@link Uncertain} is empty
     * @param <D> The return type of the {@link ThrowingReturningConsumer} task
     * @param <E> The exception type which can be thrown by the {@link ThrowingReturningConsumer} task
     * @return The result of the {@link ThrowingReturningConsumer} task or the {@link Supplier} value if {@link Uncertain} is empty
     * @throws E The exception thrown by the {@link ThrowingReturningConsumer} task
     * @author NotKili
     */
    @Nullable
    public <D, E extends Exception> D ifPresentOrElseReturn(ThrowingReturningConsumer<D, T, E> task, Supplier<D> defaultValue) throws E {
        if (isPresent()) {
            return task.accept(this.value);
        } else {
            return defaultValue.get();
        }
    }

    /**
     * Executes the provided {@link ThrowingReturningConsumer} task if the {@link Uncertain} is present, returning the result wrapped in {@link Uncertain}.
     * If absent, returns the provided {@link Supplier} value wrapped in {@link Uncertain}.
     * @param task The {@link ThrowingReturningConsumer} task to execute if the {@link Uncertain} is present
     * @param defaultValue The fallback {@link Supplier} if {@link Uncertain} is empty
     * @param <D> The return type of the {@link ThrowingReturningConsumer} task
     * @param <E> The exception type which can be thrown by the {@link ThrowingReturningConsumer} task
     * @return The result of the {@link ThrowingReturningConsumer} task wrapped in a {@link Uncertain} or the {@link Supplier} value wrapped in a {@link Uncertain} if absent
     * @throws E The exception thrown by the {@link ThrowingReturningConsumer} task
     * @author NotKili
     */
    public <D, E extends Exception> Uncertain<D> ifPresentOrElseReturnOpt(ThrowingReturningConsumer<D, T, E> task, Supplier<D> defaultValue) throws E {
        if (isPresent()) {
            return Uncertain.of(task.accept(this.value));
        } else {
            return Uncertain.of(defaultValue.get());
        }
    }
}
