package dev.notkili.basementdev.basementlib.core.util.reflection.jailbreak.impl;

import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;
import dev.notkili.basementdev.basementlib.core.util.reflection.MethodArguments;
import dev.notkili.basementdev.basementlib.core.util.reflection.MethodParameters;
import dev.notkili.basementdev.basementlib.core.util.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;

public abstract class ConstructorJailbreak<T> {
    protected final Class<T> clazz;
    protected final MethodParameters parameters;
    
    protected Constructor<T> cachedConstructor = null;

    protected ConstructorJailbreak(Class<T> clazz, Class<?>[] parameters) {
        this.clazz = clazz;
        this.parameters = new MethodParameters(parameters);
    }

    protected ConstructorJailbreak(Class<T> clazz, MethodParameters parameters) {
        this.clazz = clazz;
        this.parameters = parameters;
    }

    public static class Safe<T> extends ConstructorJailbreak<T> {
        public Safe(Class<T> clazz, Class<?>[] parameters) {
            super(clazz, parameters);
        }

        public Safe(Class<T> clazz, MethodParameters parameters) {
            super(clazz, parameters);
        }

        public Uncertain<T> newInstance(Object... args) {
            return newInstance(true, args);
        }
        
        public Uncertain<T> newInstance(boolean forceAccess, Object... args) {
            if (cachedConstructor == null) {
                var constructor = ReflectionUtil.findConstructorSafe(clazz, parameters);

                if (constructor.isMissing()) {
                    return Uncertain.empty();
                }

                cachedConstructor = constructor.get();
            }

            return ReflectionUtil.newInstanceSafe(cachedConstructor, new MethodArguments(args), forceAccess);
        }
    }

    public static class Unsafe<T> extends ConstructorJailbreak<T> {
        public Unsafe(Class<T> clazz, Class<?>[] parameters) {
            super(clazz, parameters);
        }

        public Unsafe(Class<T> clazz, MethodParameters parameters) {
            super(clazz, parameters);
        }

        public T newInstance(Object... arguments) {
            return newInstance(true, arguments);
        }
        
        public T newInstance(boolean forceAccess, Object... arguments) {
            if (cachedConstructor == null) {
                cachedConstructor = ReflectionUtil.findConstructor(clazz, parameters);
            }

            return ReflectionUtil.newInstance(cachedConstructor, new MethodArguments(arguments), forceAccess);
        }
    }
}
