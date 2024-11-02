package dev.notkili.basementdev.basementlib.core.util.registry;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.BiConsumer;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.Consumer;
import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface IRegistry<K extends RegistryKey, V> {
    String getName();

    void register(K key, V value) throws RegistryException;

    void unregister(K key) throws RegistryException;

    boolean isRegistered(K key);

    IRegistryEntry<K, V> entry(K key) throws RegistryException;

    Uncertain<IRegistryEntry<K, V>> entrySafe(K key);

    Set<IRegistryEntry<K, V>> entries();

    V value(K key) throws RegistryException;

    Uncertain<V> valueSafe(K key);

    Collection<V> values();

    Set<K> keys();

    void clear();

    int size();

    boolean isEmpty();

    default String printEntries() {
        return "[" + entries().stream().map(IRegistryEntry::print).collect(Collectors.joining(", ")) + "]";
    }

    default String printKeys() {
        return "[" + keys().stream().map(RegistryKey::print).collect(Collectors.joining(", ")) + "]";
    }

    default void forEachEntry(Consumer<IRegistryEntry<K, V>> consumer) throws RegistryException {
        for (var entry : entries()) {
            try {
                consumer.accept(entry);
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at entry [\"" + entry.toString() + "\"]: ", e);
            }
        }
    }

    default void forEachKey(Consumer<K> consumer) throws RegistryException {
        for (var key : keys()) {
            try {
                consumer.accept(key);
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at key [\"" + key.toString() + "\"]: ", e);
            }
        }
    }

    default void forEachValue(Consumer<V> consumer) throws RegistryException {
        for (var value : values()) {
            try {
                consumer.accept(value);
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at value [\"" + value.toString() + "\"]: ", e);
            }
        }
    }

    default void forEach(BiConsumer<K, V> consumer) throws RegistryException {
        for (var entry : entries()) {
            try {
                consumer.accept(entry.key(), entry.value());
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at key [\"" + entry.key() + "\"], value [\"" + entry.value() + "\"]: ", e);
            }
        }
    }
}
