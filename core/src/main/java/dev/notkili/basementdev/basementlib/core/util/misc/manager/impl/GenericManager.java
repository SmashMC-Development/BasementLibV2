package dev.notkili.basementdev.basementlib.core.util.misc.manager.impl;

import dev.notkili.basementdev.basementlib.core.util.factory.IFactory;
import dev.notkili.basementdev.basementlib.core.util.misc.key.IKey;
import dev.notkili.basementdev.basementlib.core.util.misc.manager.IGenericManager;
import dev.notkili.basementdev.basementlib.core.util.misc.provider.IProvider;
import dev.notkili.basementdev.basementlib.core.util.misc.provider.impl.Provider;
import dev.notkili.basementdev.basementlib.core.util.registry.IRegistry;

public class GenericManager<V> implements IGenericManager<V> {
    protected final String name;

    protected final IProvider<IRegistry<IKey, V>> registryProvider;

    protected final IProvider<IFactory<IKey, V>> factoryProvider;

    public GenericManager() {
        this.name = "Unknown";
        this.registryProvider = new Provider<>();
        this.factoryProvider = new Provider<>();
    }

    public GenericManager(String name) {
        this.name = name;
        this.registryProvider = new Provider<>();
        this.factoryProvider = new Provider<>();
    }

    public GenericManager(IProvider<IRegistry<IKey, V>> registryProvider, IProvider<IFactory<IKey, V>> factoryProvider) {
        this.name = "Unknown";
        this.registryProvider = registryProvider;
        this.factoryProvider = factoryProvider;
    }

    public GenericManager(String name, IProvider<IRegistry<IKey, V>> registryProvider, IProvider<IFactory<IKey, V>> factoryProvider) {
        this.name = name;
        this.registryProvider = registryProvider;
        this.factoryProvider = factoryProvider;
    }

    @Override
    public IProvider<IRegistry<IKey, V>> registryProvider() {
        return this.registryProvider;
    }

    @Override
    public IProvider<IFactory<IKey, V>> factoryProvider() {
        return this.factoryProvider;
    }

    @Override
    public String name() {
        return this.name;
    }
}
