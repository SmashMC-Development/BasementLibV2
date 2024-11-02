package dev.notkili.basementdev.basementlib.core.util.registry;

import dev.notkili.basementdev.basementlib.core.util.misc.Equatable;
import dev.notkili.basementdev.basementlib.core.util.misc.Hashable;
import dev.notkili.basementdev.basementlib.core.util.misc.Printable;

public abstract class Registrator implements Equatable, Hashable, Printable {
    @Override
    public final String toString() {
        return print();
    }

    @Override
    public final int hashCode() {
        return hash();
    }

    @Override
    public final boolean equals(Object obj) {
        return equal(obj);
    }
}
