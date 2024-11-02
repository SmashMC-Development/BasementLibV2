package dev.notkili.basementdev.basementlib.core.util.registry;

import dev.notkili.basementdev.basementlib.core.util.misc.Equatable;
import dev.notkili.basementdev.basementlib.core.util.misc.Hashable;
import dev.notkili.basementdev.basementlib.core.util.misc.Printable;

public abstract class RegistryKey implements Printable, Hashable, Equatable {
    @Override
    public int hashCode() {
        return hash();
    }

    @Override
    public boolean equals(Object obj) {
        return equal(obj);
    }

    @Override
    public String toString() {
        return print();
    }
}
