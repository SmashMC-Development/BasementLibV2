package dev.notkili.basementdev.basementlib.core.util.misc.manager.impl;

import dev.notkili.basementdev.basementlib.core.util.factory.IFactory;
import dev.notkili.basementdev.basementlib.core.util.misc.key.IKey;
import dev.notkili.basementdev.basementlib.core.util.misc.manager.IManager;
import dev.notkili.basementdev.basementlib.core.util.misc.provider.IProvider;
import dev.notkili.basementdev.basementlib.core.util.misc.provider.impl.Provider;
import dev.notkili.basementdev.basementlib.core.util.registry.IRegistry;

public class Manager<K1 extends IKey, K2 extends IKey, V> implements IManager<K1, K2, V> {
    private final String name;

    private final IProvider<IRegistry<K1, V>> registryProvider;

    private final IProvider<IFactory<K2, V>> factoryProvider;

    public Manager() {
        this.name = "Unknown";
        this.registryProvider = new Provider<>();
        this.factoryProvider = new Provider<>();
    }

    public Manager(String name) {
        this.name = name;
        this.registryProvider = new Provider<>();
        this.factoryProvider = new Provider<>();
    }

    public Manager(IProvider<IRegistry<K1, V>> registryProvider, IProvider<IFactory<K2, V>> factoryProvider) {
        this.name = "Unknown";
        this.registryProvider = registryProvider;
        this.factoryProvider = factoryProvider;
    }

    public Manager(String name, IProvider<IRegistry<K1, V>> registryProvider, IProvider<IFactory<K2, V>> factoryProvider) {
        this.name = name;
        this.registryProvider = registryProvider;
        this.factoryProvider = factoryProvider;
    }

    @Override
    public IProvider<IRegistry<K1, V>> registryProvider() {
        return this.registryProvider;
    }

    @Override
    public IProvider<IFactory<K2, V>> factoryProvider() {
        return this.factoryProvider;
    }

    @Override
    public String name() {
        return this.name;
    }
}
