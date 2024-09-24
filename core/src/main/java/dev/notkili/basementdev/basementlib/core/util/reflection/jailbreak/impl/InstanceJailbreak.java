package dev.notkili.basementdev.basementlib.core.util.reflection.jailbreak.impl;

import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;
import dev.notkili.basementdev.basementlib.core.util.reflection.MethodArguments;
import dev.notkili.basementdev.basementlib.core.util.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class InstanceJailbreak<T> {
    protected final T object;
    protected final Class<T> clazz;

    protected final Map<String, Field> fieldCache = new HashMap<>();
    protected final Map<String, Map<MethodArguments, Method>> methodCache = new HashMap<>();

    @SuppressWarnings("unchecked")
    protected InstanceJailbreak(T object) {
        this.object = object;
        this.clazz = (Class<T>) object.getClass();
    }

    protected InstanceJailbreak(T object, Class<T> clazz) {
        this.object = object;
        this.clazz = clazz;
    }
    
    public static final class Safe<T> extends InstanceJailbreak<T> {
        public Safe(T object) {
            super(object);
        }

        public Safe(T object, Class<T> clazz) {
            super(object, clazz);
        }

        public <X> Uncertain<X> getValue(String fieldName, Class<X> fieldClazz) {
            return getValue(fieldName, fieldClazz, true);
        }

        public <X> Uncertain<X> getValue(String fieldName, Class<X> fieldClazz, boolean forceAccess) {
            if (!fieldCache.containsKey(fieldName)) {
                var field = ReflectionUtil.findFieldSafe(this.clazz, fieldName);

                if (field.isMissing()) {
                    return Uncertain.empty();
                }

                fieldCache.put(fieldName, field.get());
            }

            return ReflectionUtil.getValueForFieldSafe(this.object, fieldCache.get(fieldName), fieldClazz, forceAccess);
        }

        public <X> boolean setValue(String fieldName, X value) {
            return setValue(fieldName, value, true);
        }

        public <X> boolean setValue(String fieldName, X value, boolean forceAccess) {
            if (!fieldCache.containsKey(fieldName)) {
                var field = ReflectionUtil.findFieldSafe(this.clazz, fieldName);

                if (field.isMissing()) {
                    return false;
                }

                fieldCache.put(fieldName, field.get());
            }

            return ReflectionUtil.setValueOfFieldSafe(this.object, fieldCache.get(fieldName), value, forceAccess);
        }

        public <X> boolean updateValue(String fieldName, Class<X> valueClazz, Function<X, X> updater) {
            return updateValue(fieldName, valueClazz, updater, true);
        }

        public <X> boolean updateValue(String fieldName, Class<X> valueClazz, Function<X, X> updater, boolean forceAccess) {
            if (!fieldCache.containsKey(fieldName)) {
                var field = ReflectionUtil.findFieldSafe(this.clazz, fieldName);

                if (field.isMissing()) {
                    return false;
                }

                fieldCache.put(fieldName, field.get());
            }

            return ReflectionUtil.modifyValueOfFieldSafe(this.object, fieldCache.get(fieldName), valueClazz, updater, forceAccess);
        }

        public <X> Uncertain<X> invokeMethod(String methodName, Class<X> returnClazz, Object... args) {
            return invokeMethod(methodName, returnClazz, true, args);
        }

        public <X> Uncertain<X> invokeMethod(String methodName, Class<X> returnClazz, boolean forceAccess, Object... args) {
            if (!methodCache.containsKey(methodName)) {
                methodCache.put(methodName, new HashMap<>());
            }

            var cache = methodCache.get(methodName);
            var arguments = new MethodArguments(args);

            if (!cache.containsKey(arguments)) {
                var method = ReflectionUtil.findMethodSafe(this.clazz, methodName, arguments.convertToParameters());

                if (method.isMissing()) {
                    return Uncertain.empty();
                }

                cache.put(arguments, method.get());
            }

            return ReflectionUtil.invokeMethodSafe(this.object, methodCache.get(methodName).get(arguments), returnClazz, arguments, forceAccess);
        }
    }

    public static final class Unsafe<T> extends InstanceJailbreak<T> {
        public Unsafe(T object) {
            super(object);
        }

        public Unsafe(T object, Class<T> clazz) {
            super(object, clazz);
        }

        public <X> X getValue(String fieldName, Class<X> fieldClazz) {
            return getValue(fieldName, fieldClazz, true);
        }

        public <X> X getValue(String fieldName, Class<X> fieldClazz, boolean forceAccess) {
            if (!fieldCache.containsKey(fieldName)) {
                fieldCache.put(fieldName, ReflectionUtil.findField(this.clazz, fieldName));
            }

            return ReflectionUtil.getValueForField(this.object, fieldCache.get(fieldName), fieldClazz, forceAccess);
        }

        public <X> void setValue(String fieldName, X value) {
            setValue(fieldName, value, true);
        }

        public <X> void setValue(String fieldName, X value, boolean forceAccess) {
            if (!fieldCache.containsKey(fieldName)) {
                fieldCache.put(fieldName, ReflectionUtil.findField(this.clazz, fieldName));
            }

            ReflectionUtil.setValueOfField(this.object, fieldCache.get(fieldName), value, forceAccess);
        }

        public <X> void updateValue(String fieldName, Class<X> valueClazz, Function<X, X> updater) {
            updateValue(fieldName, valueClazz, updater, true);
        }

        public <X> void updateValue(String fieldName, Class<X> valueClazz, Function<X, X> updater, boolean forceAccess) {
            if (!fieldCache.containsKey(fieldName)) {
                fieldCache.put(fieldName, ReflectionUtil.findField(this.clazz, fieldName));
            }

            ReflectionUtil.modifyValueOfField(this.object, fieldCache.get(fieldName), valueClazz, updater, forceAccess);
        }

        public <X> X invokeMethod(String methodName, Class<X> returnClazz, Object... args) {
            return invokeMethod(methodName, returnClazz, true, args);
        }

        public <X> X invokeMethod(String methodName, Class<X> returnClazz, boolean forceAccess, Object... args) {
            if (!methodCache.containsKey(methodName)) {
                methodCache.put(methodName, new HashMap<>());
            }

            var cache = methodCache.get(methodName);
            var arguments = new MethodArguments(args);

            if (!cache.containsKey(arguments)) {
                cache.put(arguments, ReflectionUtil.findMethod(this.clazz, methodName, arguments.convertToParameters()));
            }

            return ReflectionUtil.invokeMethod(this.object, methodCache.get(methodName).get(arguments), returnClazz, arguments, forceAccess);
        }
    }
}
