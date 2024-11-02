package dev.notkili.basementdev.basementlib.core.util.registry;

public interface IOwnedRegistryEntry<K extends RegistryKey, V> extends IRegistryEntry<K, V> {
    Registrator registrator();

    @Override
    default String print() {
        return "[Entry]: \"" + key().print() + "\", registered by \" " + registrator().print() + " \"";
    }
}
