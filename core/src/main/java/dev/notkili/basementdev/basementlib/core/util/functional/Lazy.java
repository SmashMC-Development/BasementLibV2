package dev.notkili.basementdev.basementlib.core.util.functional;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.supplier.safe.ThrowingSupplier;
import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public interface Lazy<T> extends ThrowingSupplier<T, Exception> {
    Uncertain<T> getSafe();
    boolean isInitialized();

    interface MutableLazy<T> extends Lazy<T> {
        void invalidate();
        void set(T value);
    }

    static <T> Lazy<T> of(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new Fast<>(supplier);
    }

    static <T> Lazy<T> concurrent(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new Concurrent<>(supplier);
    }

    static <T> MutableLazy<T> mutable(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new Mutable<>(supplier);
    }

    static <T> MutableConcurrent<T> mutableConcurrent(ThrowingSupplier<T, ? extends Exception> supplier)
    {
        return new MutableConcurrent<>(supplier);
    }

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
