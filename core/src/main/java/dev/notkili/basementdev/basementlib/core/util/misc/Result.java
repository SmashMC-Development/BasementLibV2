package dev.notkili.basementdev.basementlib.core.util.misc;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.safe.ThrowingConsumer;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.Consumer;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.safe.ThrowingFunction;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.function.unsafe.Function;
import lombok.NonNull;

import javax.annotation.Nullable;

/**
 * Represents a result of an operation with either a value or an error.
 * This can be used to avoid throwing exceptions and instead handle them in a more functional and "safer" manner.
 * Use {@link #success(Object)} to create a successful result and {@link #failure(Exception)} to create a failed result.
 * @param <R> The type of the successful result
 * @param <E> The type of the exception that can be thrown
 * @author NotKili
 * @since 1.0.0
 */
public class Result<R, E extends Exception> {
    /**
     * Creates a successful {@link Result} object with the given value.
     * @param result The value of the successful result
     * @return A new {@link Result} object with the given value
     * @param <R> The type of the successful result
     * @param <E> The type of the exception that can be thrown
     * @author NotKili
     * @since 1.0.0
     */
    public static <R, E extends Exception> Result<R, E> success(@NonNull R result) {
        return new Result<>(result);
    }

    /**
     * Creates a failed {@link Result} object with the given error.
     * @param error The error that caused the operation to fail
     * @return A new {@link Result} object with the given error
     * @param <R> The type of the successful result
     * @param <E> The type of the exception that can be thrown
     * @author NotKili
     * @since 1.0.0
     */
    public static <R, E extends Exception> Result<R, E> failure(@NonNull E error) {
        return new Result<>(error);
    }
    
    private final R result;
    private final E error;

    private Result(@NonNull E error) {
        this.result = null;
        this.error = error;
    }

    private Result(@NonNull R result) {
        this.result = result;
        this.error = null;
    }

    /**
     * Returns whether the operation was successful or not.
     * @return {@code true} if the operation was successful, {@code false} otherwise
     * @author NotKili
     * @since 1.0.0
     */
    public boolean successful() {
        return error == null;
    }

    /**
     * Returns the value if the operation was successful. Throws any present error wrapped in a {@link RuntimeException} if the operation failed.
     * @return The result of the operation
     * @author NotKili
     * @since 1.0.0
     */
    public R unwrap() {
        if (error != null) {
            throw new RuntimeException(error);
        }
        
        return result;
    }

    /**
     * Returns the present error if the operation failed. Returns {@code null} if the operation was successful.
     * @return The error that caused the operation to fail, or {@code null} if the operation was successful
     * @author NotKili
     * @since 1.0.0
     */
    @Nullable
    public E error() {
        return error;
    }

    /**
     * Returns the value wrapped in a {@link Uncertain} object if the operation was successful. Returns an empty {@link Uncertain} object if the operation failed.
     * @return The result of the operation wrapped in a {@link Uncertain} object, or an empty {@link Uncertain} object if the operation failed
     * @author NotKili
     * @since 1.0.0
     */
    public Uncertain<R> unwrapSafe() {
        if (error != null) {
            return Uncertain.empty();
        }

        return Uncertain.of(result);
    }

    /**
     * Returns the present error wrapped in a {@link Uncertain} object if the operation failed. Returns an empty {@link Uncertain} object if the operation was successful.
     * @return The error that caused the operation to fail wrapped in a {@link Uncertain} object, or an empty {@link Uncertain} object if the operation was successful
     * @author NotKili
     * @since 1.0.0
     */
    public Uncertain<E> errorSafe() {
        if (error != null) {
            return Uncertain.of(error);
        }

        return Uncertain.empty();
    }

    /**
     * Returns the value if the operation was successful. Returns the given default value if the operation failed.
     * @param other The default value to return if the operation failed
     * @return The result of the operation, or the given default value if the operation failed
     * @author NotKili
     * @since 1.0.0
     */
    public R or(@NonNull R other) {
        return result == null ? other : result;
    }

    /**
     * Calls the given {@link Consumer} using the value if the operation was successful.
     * @param resultConsumer The {@link Consumer} to call using the value if the operation was successful
     * @author NotKili
     * @since 1.0.0
     */
    public void onSuccess(@NonNull Consumer<R> resultConsumer) {
        if (error == null) {
            resultConsumer.accept(result);
        }
    }

    /**
     * Calls the given {@link ThrowingConsumer} with the value if the operation was successful. If an error occurs while calling the consumer, it will be thrown.
     * @param resultConsumer The {@link ThrowingConsumer} to call with the value if the operation was successful
     * @param <E2> The type of the exception that can be thrown
     * @throws E2 The exception thrown by the {@link ThrowingConsumer}
     * @author NotKili
     * @since 1.0.0
     */
    public <E2 extends Exception> void onSuccess(@NonNull ThrowingConsumer<R, E2> resultConsumer) throws E2 {
        if (error == null) {
            resultConsumer.accept(result);
        }
    }

    /**
     * Calls the given {@link Consumer} using the error if the operation failed.
     * @param errorConsumer The {@link Consumer} to call using the error if the operation failed
     * @author NotKili
     * @since 1.0.0
     */
    public void onError(@NonNull Consumer<E> errorConsumer) {
        if (error != null) {
            errorConsumer.accept(error);
        }
    }

    /**
     * Calls the given {@link ThrowingConsumer} using the error if the operation failed. If an error occurs while calling the consumer, it will be thrown.
     * @param errorConsumer The {@link ThrowingConsumer} to call using the error if the operation failed
     * @param <E2> The type of the exception that can be thrown
     * @throws E2 The exception thrown by the {@link ThrowingConsumer}
     * @author NotKili
     * @since 1.0.0
     */
    public <E2 extends Exception> void onError(@NonNull ThrowingConsumer<E, E2> errorConsumer) throws E2 {
        if (error != null) {
            errorConsumer.accept(error);
        }
    }

    /**
     * Calls the given {@link Consumer} using the value if the operation was successful, or the given {@link Consumer} using the error if the operation failed.
     * @param resultConsumer The {@link Consumer} to call using the value if the operation was successful
     * @param errorConsumer The {@link Consumer} to call using the error if the operation failed
     * @author NotKili
     * @since 1.0.0
     */
    public void then(@NonNull Consumer<R> resultConsumer, @NonNull Consumer<E> errorConsumer) {
        if (error != null) {
            errorConsumer.accept(error);
        } else {
            resultConsumer.accept(result);
        }
    }

    /**
     * Calls the given {@link ThrowingConsumer} using the value if the operation was successful, or the given {@link Consumer} using the error if the operation failed.
     * If an error occurs while calling the resultConsumer, it will be thrown.
     * @param resultConsumer The {@link ThrowingConsumer} to call using the value if the operation was successful
     * @param errorConsumer The {@link Consumer} to call using the error if the operation failed
     * @param <E2> The type of the exception that can be thrown by the {@link ThrowingConsumer}
     * @throws E2 The exception thrown by the {@link ThrowingConsumer}
     * @author NotKili
     * @since 1.0.0
     */
    public <E2 extends Exception> void then(@NonNull ThrowingConsumer<R, E2> resultConsumer, @NonNull Consumer<E> errorConsumer) throws E2 {
        if (error != null) {
            errorConsumer.accept(error);
        } else {
            resultConsumer.accept(result);
        }
    }

    /**
     * Calls the given {@link Consumer} using the value if the operation was successful, or the given {@link ThrowingConsumer} using the error if the operation failed.
     * If an error occurs while calling the errorConsumer, it will be thrown.
     * @param resultConsumer The {@link Consumer} to call using the value if the operation was successful
     * @param errorConsumer The {@link ThrowingConsumer} to call using the error if the operation failed
     * @param <E2> The type of the exception that can be thrown by the {@link ThrowingConsumer}
     * @throws E2 The exception thrown by the {@link ThrowingConsumer}
     * @author NotKili
     * @since 1.0.0
     */
    public <E2 extends Exception> void then(@NonNull Consumer<R> resultConsumer, @NonNull ThrowingConsumer<E, E2> errorConsumer) throws E2 {
        if (error != null) {
            errorConsumer.accept(error);
        } else {
            resultConsumer.accept(result);
        }
    }

    /**
     * Calls the given {@link ThrowingConsumer} using the value if the operation was successful, or the given {@link ThrowingConsumer} using the error if the operation failed.
     * If an error occurs while calling any consumer, it will be thrown.
     * @param resultConsumer The {@link ThrowingConsumer} to call using the value if the operation was successful
     * @param errorConsumer The {@link ThrowingConsumer} to call using the error if the operation failed
     * @param <E2> The type of the exception that can be thrown by the {@link ThrowingConsumer} for the value
     * @param <E3> The type of the exception that can be thrown by the {@link ThrowingConsumer} for the error
     * @throws E2 The exception thrown by the {@link ThrowingConsumer} for the value
     * @throws E3 The exception thrown by the {@link ThrowingConsumer} for the error
     * @author NotKili
     * @since 1.0.0
     */
    public <E2 extends Exception, E3 extends Exception> void then(@NonNull ThrowingConsumer<R, E2> resultConsumer, @NonNull ThrowingConsumer<E, E3> errorConsumer) throws E2, E3 {
        if (error != null) {
            errorConsumer.accept(error);
        } else {
            resultConsumer.accept(result);
        }
    }

    /**
     * Maps the value of the {@link Result} object using the given {@link Function} if the operation was successful.
     * @param mapper The {@link Function} to use to map the value
     * @return A new {@link Result} object with the mapped value, or the error if the operation failed
     * @param <T> The type of the new value
     * @author NotKili
     * @since 1.0.0
     */
    public <T> Result<T, E> map(@NonNull Function<R, T> mapper) {
        if (error != null) {
            return failure(error);
        }
        
        return success(mapper.apply(result));
    }

    /**
     * Maps the value of the {@link Result} object using the given {@link ThrowingFunction} if the operation was successful.
     * If an error occurs while mapping the value, it will be returned as an error in the {@link Result} object.
     * @param mapper The {@link ThrowingFunction} to use to map the value
     * @return A new {@link Result} object with the mapped value, or the error if the operation failed
     * @param <T> The type of the new value
     * @author NotKili
     * @since 1.0.0
     */
    public <T> Result<T, ? extends Exception> map(@NonNull ThrowingFunction<R, T, ? extends Exception> mapper) {
        if (error != null) {
            return failure(error);
        }

        try {
            return success(mapper.apply(result));
        } catch (Exception e) {
            return failure(e);
        }
    }

    /**
     * Maps the value of the {@link Result} object using the given {@link ThrowingFunction} if the operation was successful.
     * @param mapper The {@link ThrowingFunction} to use to map the value
     * @return A new {@link Result} object with the mapped value, or the error if the operation failed
     * @param <T> The type of the new value
     * @author NotKili
     * @since 1.0.0
     */
    public <T> Result<T, ? extends Exception> mapFlat(@NonNull Function<R, Result<T, ? extends Exception>> mapper) {
        if (error != null) {
            return failure(error);
        }
        
        return mapper.apply(result);
    }

    /**
     * Maps the value of the {@link Result} object using the given {@link ThrowingFunction} if the operation was successful.
     * If an error occurs while mapping the value, it will be returned as an error in the {@link Result} object.
     * @param mapper The {@link ThrowingFunction} to use to map the value
     * @return A new {@link Result} object with the mapped value, or the error if the operation failed
     * @param <T> The type of the new value
     * @author NotKili
     * @since 1.0.0
     */
    public <T> Result<T, ? extends Exception> mapFlat(@NonNull ThrowingFunction<R, Result<T, ? extends Exception>, ? extends Exception> mapper) {
        if (error != null) {
            return failure(error);
        }
        
        try {
            return mapper.apply(result);
        } catch (Exception e) {
            return failure(e);
        }
    }
}
