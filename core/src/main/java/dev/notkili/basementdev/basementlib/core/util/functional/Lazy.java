package dev.notkili.basementdev.basementlib.core.util.functional;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.supplier.safe.ThrowingSupplier;
import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Wrapper for lazily initialized values. <br>
 * Use: <br>
 * - {@link #of(ThrowingSupplier)} for single-threaded initialization <br>
 * - {@link #concurrent(ThrowingSupplier)} for concurrent safe initialization <br>
 * - {@link #mutable(ThrowingSupplier)} for single-threaded initialization, supports invalidation<br>
 * - {@link #mutableConcurrent(ThrowingSupplier)} for concurrent safe initialization, supports invalidation<br>
 * @param <T> The type of the lazily computed value
 * @author NotKili
 */
public interface Lazy<T> extends ThrowingSupplier<T, Exception> {
    /**
     * Tries to compute the value if it hasn't been computed yet and returns it wrapped in an {@link Uncertain} <br>
     * If any exception is thrown during computation, an empty {@link Uncertain} is returned.
     * @return The value wrapped in an {@link Uncertain}
     * @author NotKili
     */
    Uncertain<T> getSafe();
    
    /**
     * Checks if the value has been initialized.
     * @return {@code true} if the value has been initialized, {@code false} otherwise
     * @author NotKili
     */
    boolean isInitialized();

    /**
     * Mutable version of {@link Lazy}. <br>
     * Use: <br>
     * - {@link #mutable(ThrowingSupplier)} for single-threaded initialization<br>
     * - {@link #mutableConcurrent(ThrowingSupplier)} for concurrent safe initialization<br>
     * @param <T> The type of the lazily computed value
     * @see Lazy
     * @author NotKili 
     */
    interface MutableLazy<T> extends Lazy<T> {
        /**
         * Invalidates the value, forcing it to be recomputed on the next {@link #get()} or {@link #getSafe()} call.
         * @author NotKili
         */
        void invalidate();
        
        /**
         * Sets the value to a new value.
         * @param value The new value
         * @author NotKili
         */
        void set(T value);
    }

    /**
     * Creates a new {@link Fast} lazy instance with the given supplier. <br>
     * The returned lazy instance is immutable and not concurrent safe.
     * @param supplier The supplier to use for value initialization
     * @return A new {@link Fast} lazy instance
     * @param <T> The type of the lazily computed value
     * @see Fast
     * @author NotKili
     */
    static <T> Lazy<T> of(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new Fast<>(supplier);
    }

    /**
     * Creates a new {@link Concurrent} lazy instance with the given supplier. <br>
     * The returned lazy instance is immutable and concurrent safe.
     * @param supplier The supplier to use for value initialization
     * @return A new {@link Concurrent} lazy instance
     * @param <T> The type of the lazily computed value
     * @see Concurrent
     * @author NotKili
     */
    static <T> Lazy<T> concurrent(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new Concurrent<>(supplier);
    }

    /**
     * Creates a new {@link Mutable} lazy instance with the given supplier. <br>
     * The returned lazy instance is mutable and not concurrent safe.
     * @param supplier The supplier to use for value initialization
     * @return A new {@link Mutable} lazy instance
     * @param <T> The type of the lazily computed value
     * @see Mutable
     * @author NotKili
     */
    static <T> MutableLazy<T> mutable(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new Mutable<>(supplier);
    }

    /**
     * Creates a new {@link MutableConcurrent} lazy instance with the given supplier. <br>
     * The returned lazy instance is mutable and concurrent safe.
     * @param supplier The supplier to use for value initialization
     * @return A new {@link MutableConcurrent} lazy instance
     * @param <T> The type of the lazily computed value
     * @see MutableConcurrent
     * @author NotKili
     */
    static <T> MutableConcurrent<T> mutableConcurrent(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new MutableConcurrent<>(supplier);
    }

    /**
     * Immutable, single-threaded implementation of {@link Lazy}. <br>
     * @param <T> The type of the lazily computed value
     * @author NotKili
     */
    final class Fast<T> implements Lazy<T>
    {
        private ThrowingSupplier<T, ? extends Exception> supplier;
        
        private T instance;

        private Fast(@Nonnull ThrowingSupplier<T, ? extends Exception> supplier)
        {
            this.supplier = supplier;
        }

        @Override
        public T get() throws Exception {
            if (supplier != null)
            {
                instance = supplier.get();
                supplier = null;
            }

            return instance;
        }

        @Override
        public Uncertain<T> getSafe() {
            try {
                return Uncertain.of(get());
            } catch (Exception e) {
                return Uncertain.empty();
            }
        }

        @Override
        public boolean isInitialized() {
            return supplier == null;
        }
    }

    /**
     * Immutable, concurrent safe implementation of {@link Lazy}. <br>
     * @param <T> The type of the lazily computed value
     * @author NotKili
     */
    final class Concurrent<T> implements Lazy<T>
    {
        private volatile Object lock = new Object();
        private volatile ThrowingSupplier<T, ? extends Exception> supplier;
        private volatile T instance;

        private Concurrent(ThrowingSupplier<T, ? extends Exception> supplier)
        {
            this.supplier = supplier;
        }

        @Nullable
        @Override
        public T get() throws Exception
        {
            Object localLock = lock;
            if (supplier != null)
            {
                synchronized (localLock)
                {
                    if (supplier != null)
                    {
                        instance = supplier.get();
                        supplier = null;
                        lock = null;
                    }
                }
            }
            return instance;
        }

        @Override
        public Uncertain<T> getSafe() {
            try {
                return Uncertain.of(get());
            } catch (Exception e) {
                return Uncertain.empty();
            }
        }

        @Override
        public boolean isInitialized() {
            return this.supplier != null;
        }
    }

    /**
     * Mutable, single-threaded implementation of {@link MutableLazy}. <br>
     * @param <T> The type of the lazily computed value
     * @author NotKili
     */
    final class Mutable<T> implements MutableLazy<T>
    {
        private final ThrowingSupplier<T, ? extends Exception> supplier;
        private T instance;

        private Mutable(@Nonnull ThrowingSupplier<T, ? extends Exception> supplier)
        {
            this.supplier = supplier;
        }

        @Override
        public T get() throws Exception {
            if (instance == null) {
                instance = supplier.get();
            }

            return instance;
        }

        @Override
        public Uncertain<T> getSafe() {
            try {
                return Uncertain.of(get());
            } catch (Exception e) {
                return Uncertain.empty();
            }
        }

        @Override
        public boolean isInitialized() {
            return instance != null;
        }

        @Override
        public void invalidate() {
            this.instance = null;
        }

        @Override
        public void set(T value) {
            this.instance = value;
        }
    }

    /**
     * Mutable, concurrent safe implementation of {@link MutableLazy}. <br>
     * @param <T> The type of the lazily computed value
     * @author NotKili
     */
    final class MutableConcurrent<T> implements MutableLazy<T> {
        private final Lock lock = new ReentrantLock();
        private final ThrowingSupplier<T, ? extends Exception> supplier;
        private volatile T instance;

        public MutableConcurrent(ThrowingSupplier<T, ? extends Exception> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() throws Exception {
            if (instance == null)
            {
                lock.lock();

                try {
                    if (instance == null) {
                        instance = supplier.get();
                    }
                } finally {
                    lock.unlock();
                }
            }

            return instance;
        }

        @Override
        public Uncertain<T> getSafe() {
            try {
                return Uncertain.of(get());
            } catch (Exception e) {
                return Uncertain.empty();
            }
        }

        @Override
        public boolean isInitialized() {
            return this.instance != null;
        }

        @Override
        public void invalidate() {
            lock.lock();
            this.instance = null;
            lock.unlock();
        }

        @Override
        public void set(T value) {
            lock.lock();
            this.instance = value;
            lock.unlock();
        }
    }
}
