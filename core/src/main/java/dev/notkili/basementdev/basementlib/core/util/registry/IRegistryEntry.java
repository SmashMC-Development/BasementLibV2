package dev.notkili.basementdev.basementlib.core.util.registry;

import dev.notkili.basementdev.basementlib.core.util.misc.Equatable;
import dev.notkili.basementdev.basementlib.core.util.misc.Hashable;
import dev.notkili.basementdev.basementlib.core.util.misc.Printable;

public interface IRegistryEntry<K extends RegistryKey, V> extends Printable, Hashable, Equatable {
    K key();

    V value();

    @Override
    default boolean equal(Object obj) {
        return key().equal(obj);
    }

    @Override
    default int hash() {
        return key().hash();
    }

    @Override
    default String print() {
        return "[Entry]: " + key().print();
    }
}
