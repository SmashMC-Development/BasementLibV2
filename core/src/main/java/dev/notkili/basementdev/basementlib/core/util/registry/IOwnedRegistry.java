package dev.notkili.basementdev.basementlib.core.util.registry;

import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.BiConsumer;
import dev.notkili.basementdev.basementlib.core.util.functional.lambda.consumer.unsafe.Consumer;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface IOwnedRegistry<K extends RegistryKey, V> extends IRegistry<K, V> {
    void register(Registrator registrator, K key, V value) throws RegistryException;

    boolean isRegistered(Registrator registrator, K key);

    boolean hasRegistered(Registrator registrator);

    @Override
    boolean isRegistered(K key);

    Set<IRegistryEntry<K, V>> entries(Registrator registrator);

    Set<Registrator> registrators();

    Collection<V> values(Registrator registrator);

    Set<K> keys(Registrator registrator);

    void clear(Registrator registrator);

    int size(Registrator registrator);

    boolean isEmpty(Registrator registrator);

    default String printEntries(Registrator registrator) {
        return "[" + entries(registrator).stream().map(IRegistryEntry::print).collect(Collectors.joining(", ")) + "]";
    }

    default String printKeys(Registrator registrator) {
        return "[" + keys(registrator).stream().map(RegistryKey::print).collect(Collectors.joining(", ")) + "]";
    }

    default void forEachEntry(Registrator registrator, Consumer<IRegistryEntry<K, V>> consumer) throws RegistryException {
        for (var entry : entries(registrator)) {
            try {
                consumer.accept(entry);
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at entry [\"" + entry.toString() + "\"]: ", e);
            }
        }
    }

    default void forEachKey(Registrator registrator, Consumer<K> consumer) throws RegistryException {
        for (var key : keys(registrator)) {
            try {
                consumer.accept(key);
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at key [\"" + key.toString() + "\"]: ", e);
            }
        }
    }

    default void forEachValue(Registrator registrator, Consumer<V> consumer) throws RegistryException {
        for (var value : values(registrator)) {
            try {
                consumer.accept(value);
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at value [\"" + value.toString() + "\"]: ", e);
            }
        }
    }

    default void forEach(Registrator registrator, BiConsumer<K, V> consumer) throws RegistryException {
        for (var entry : entries(registrator)) {
            try {
                consumer.accept(entry.key(), entry.value());
            } catch (Exception e) {
                throw new RegistryException(this, "An exception occurred at key [\"" + entry.key() + "\"], value [\"" + entry.value() + "\"]: ", e);
            }
        }
    }
}
