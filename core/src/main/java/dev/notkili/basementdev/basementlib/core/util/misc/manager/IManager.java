package dev.notkili.basementdev.basementlib.core.util.misc.manager;

import dev.notkili.basementdev.basementlib.core.util.factory.FactoryException;
import dev.notkili.basementdev.basementlib.core.util.factory.IFactory;
import dev.notkili.basementdev.basementlib.core.util.factory.IFactoryParameter;
import dev.notkili.basementdev.basementlib.core.util.factory.impl.parameter.NoParameter;
import dev.notkili.basementdev.basementlib.core.util.misc.INamed;
import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;
import dev.notkili.basementdev.basementlib.core.util.misc.key.IKey;
import dev.notkili.basementdev.basementlib.core.util.misc.provider.IProvider;
import dev.notkili.basementdev.basementlib.core.util.registry.IRegistry;
import dev.notkili.basementdev.basementlib.core.util.registry.RegistryException;

public interface IManager<K1 extends IKey, K2 extends IKey, V> extends INamed {
    IProvider<IRegistry<K1, V>> registryProvider();

    default IRegistry<K1, V> registry() {
        return registryProvider().instance();
    }


    IProvider<IFactory<K2, V>> factoryProvider();

    default IFactory<K2, V> factory() {
        return factoryProvider().instance();
    }


    default V remove(K1 registryKey) throws ManagerException {
        var reg = registry();

        if (!reg.isRegistered(registryKey)) {
            throw new ManagerException(this, "Instance with key \"" + registryKey.print() + "\" does not exist");
        }

        try {
            var tmp = reg.value(registryKey);
            reg.unregister(registryKey);
            return tmp;
        } catch (RegistryException e) {
            throw new ManagerException(this, e.getOriginalMessage(), e);
        }
    }


    default V compute(K1 registryKey, K2 factoryKey) throws ManagerException {
        return compute(registryKey, factoryKey, NoParameter.INSTANCE);
    }

    default <P extends IFactoryParameter> V compute(K1 registryKey, K2 factoryKey, P factoryParameter) throws ManagerException {
        var reg = registry();

        if (reg.isRegistered(registryKey)) {
            throw new ManagerException(this, "Instance with key \"" + registryKey.print() + "\" already exists");
        }

        var fac = factory();

        if (!fac.isRegistered(factoryKey)) {
            throw new ManagerException(this, "Factory with key \"" + factoryKey.print() + "\" does not exist");
        }

        try {
            var instance = fac.create(factoryKey, factoryParameter);
            reg.register(registryKey, instance);
            return instance;
        } catch (FactoryException | RegistryException e) {
            throw new ManagerException(this, e.getOriginalMessage(), e);
        }
    }

    default <V2 extends V> V2 compute(K1 registryKey, K2 factoryKey, Class<V2> clazz) throws ManagerException {
        return compute(registryKey, factoryKey, clazz, NoParameter.INSTANCE);
    }

    default <V2 extends V, P extends IFactoryParameter> V2 compute(K1 registryKey, K2 factoryKey, Class<V2> clazz, P factoryParameter) throws ManagerException {
        var reg = registry();

        if (reg.isRegistered(registryKey)) {
            throw new ManagerException(this, "Instance with key \"" + registryKey.print() + "\" already exists");
        }

        var fac = factory();

        if (!fac.isRegistered(factoryKey)) {
            throw new ManagerException(this, "Factory with key \"" + factoryKey.print() + "\" does not exist");
        }

        try {
            var instance = fac.create(factoryKey, clazz, factoryParameter);
            reg.register(registryKey, instance);
            return instance;
        } catch (FactoryException | RegistryException e) {
            throw new ManagerException(this, e.getOriginalMessage(), e);
        }
    }


    default Uncertain<V> computeSafe(K1 registryKey, K2 factoryKey) {
        return computeSafe(registryKey, factoryKey, NoParameter.INSTANCE);
    }

    default <P extends IFactoryParameter> Uncertain<V> computeSafe(K1 registryKey, K2 factoryKey, P factoryParameter) {
        var reg = registry();

        if (reg.isRegistered(registryKey)) {
            return Uncertain.empty();
        }

        var fac = factory();

        if (!fac.isRegistered(factoryKey)) {
            return Uncertain.empty();
        }

        return fac.createSafe(factoryKey, factoryParameter)
                .map(instance -> {
                    try {
                        reg.register(registryKey, instance);
                        return instance;
                    } catch (RegistryException e) {
                        return null;
                    }
                });
    }

    default <C extends V> Uncertain<C> computeSafe(K1 registryKey, K2 factoryKey, Class<C> clazz) {
        return computeSafe(registryKey, factoryKey, clazz, NoParameter.INSTANCE);
    }

    default <C extends V, P extends IFactoryParameter> Uncertain<C> computeSafe(K1 registryKey, K2 factoryKey, Class<C> clazz, P factoryParameter) {
        var reg = registry();

        if (reg.isRegistered(registryKey)) {
            return Uncertain.empty();
        }

        var fac = factory();

        if (!fac.isRegistered(factoryKey)) {
            return Uncertain.empty();
        }

        return fac.createSafe(factoryKey, clazz, factoryParameter)
                .map(instance -> {
                    try {
                        reg.register(registryKey, instance);
                        return instance;
                    } catch (RegistryException e) {
                        return null;
                    }
                });
    }


    default V computeIfAbsent(K1 registryKey, K2 factoryKey) throws ManagerException {
        return computeIfAbsent(registryKey, factoryKey, NoParameter.INSTANCE);
    }

    default <P extends IFactoryParameter> V computeIfAbsent(K1 registryKey, K2 factoryKey, P factoryParameter) throws ManagerException {
        var reg = registry();

        if (reg.isRegistered(registryKey)) {
            return reg.value(registryKey);
        }

        var fac = factory();

        if (!fac.isRegistered(factoryKey)) {
            throw new ManagerException(this, "Factory with key \"" + factoryKey.print() + "\" does not exist");
        }

        try {
            var instance = fac.create(factoryKey, factoryParameter);
            reg.register(registryKey, instance);
            return instance;
        } catch (FactoryException | RegistryException e) {
            throw new ManagerException(this, e.getOriginalMessage(), e);
        }
    }

    default <V2 extends V> V2 computeIfAbsent(K1 registryKey, K2 factoryKey, Class<V2> clazz) throws ManagerException {
        return computeIfAbsent(registryKey, factoryKey, clazz, NoParameter.INSTANCE);
    }

    default <V2 extends V, P extends IFactoryParameter> V2 computeIfAbsent(K1 registryKey, K2 factoryKey, Class<V2> clazz, P factoryParameter) throws ManagerException {
        var reg = registry();

        if (reg.isRegistered(registryKey)) {
            try {
                return clazz.cast(reg.value(registryKey));
            } catch (ClassCastException e) {
                throw new ManagerException(this, "Instance with key \"" + registryKey.print() + "\" is not of type " + clazz.getName());
            }
        }

        var fac = factory();

        if (!fac.isRegistered(factoryKey)) {
            throw new ManagerException(this, "Factory with key \"" + factoryKey.print() + "\" does not exist");
        }

        try {
            var instance = fac.create(factoryKey, clazz, factoryParameter);
            reg.register(registryKey, instance);
            return instance;
        } catch (FactoryException | RegistryException e) {
            throw new ManagerException(this, e.getOriginalMessage(), e);
        }
    }
}
