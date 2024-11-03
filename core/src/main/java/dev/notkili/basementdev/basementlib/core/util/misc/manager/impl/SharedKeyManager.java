package dev.notkili.basementdev.basementlib.core.util.misc.manager.impl;

import dev.notkili.basementdev.basementlib.core.util.factory.IFactory;
import dev.notkili.basementdev.basementlib.core.util.misc.key.IKey;
import dev.notkili.basementdev.basementlib.core.util.misc.manager.ISharedKeyManager;
import dev.notkili.basementdev.basementlib.core.util.misc.provider.IProvider;
import dev.notkili.basementdev.basementlib.core.util.misc.provider.impl.Provider;
import dev.notkili.basementdev.basementlib.core.util.registry.IRegistry;

public class SharedKeyManager<K extends IKey, V> implements ISharedKeyManager<K, V> {
    private final String name;

    private final IProvider<IRegistry<K, V>> registryProvider;

    private final IProvider<IFactory<K, V>> factoryProvider;

    public SharedKeyManager() {
        this.name = "Unknown";
        this.registryProvider = new Provider<>();
        this.factoryProvider = new Provider<>();
    }

    public SharedKeyManager(String name) {
        this.name = name;
        this.registryProvider = new Provider<>();
        this.factoryProvider = new Provider<>();
    }

    public SharedKeyManager(IProvider<IRegistry<K, V>> registryProvider, IProvider<IFactory<K, V>> factoryProvider) {
        this.name = "Unknown";
        this.registryProvider = registryProvider;
        this.factoryProvider = factoryProvider;
    }

    public SharedKeyManager(String name, IProvider<IRegistry<K, V>> registryProvider, IProvider<IFactory<K, V>> factoryProvider) {
        this.name = name;
        this.registryProvider = registryProvider;
        this.factoryProvider = factoryProvider;
    }

    @Override
    public IProvider<IRegistry<K, V>> registryProvider() {
        return this.registryProvider;
    }

    @Override
    public IProvider<IFactory<K, V>> factoryProvider() {
        return this.factoryProvider;
    }

    @Override
    public String name() {
        return this.name;
    }
}
