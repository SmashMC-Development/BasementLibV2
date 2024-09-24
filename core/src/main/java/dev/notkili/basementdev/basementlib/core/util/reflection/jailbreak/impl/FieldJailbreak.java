package dev.notkili.basementdev.basementlib.core.util.reflection.jailbreak.impl;

import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;
import dev.notkili.basementdev.basementlib.core.util.reflection.ReflectionUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

public abstract class FieldJailbreak<T, E> {
    protected final Class<T> clazz;
    protected final String fieldName;
    protected final Class<E> fieldClazz;

    protected Field cachedField = null;

    protected FieldJailbreak(Class<T> clazz, String fieldName, Class<E> fieldClazz) {
        this.clazz = clazz;
        this.fieldName = fieldName;
        this.fieldClazz = fieldClazz;
    }

    @Getter
    @Setter
    public static abstract class Instance<T, E> extends FieldJailbreak<T, E> {
        protected T caller;

        @SuppressWarnings("unchecked")
        protected Instance(T caller, String fieldName, Class<E> fieldClazz) {
            super((Class<T>) caller.getClass(), fieldName, fieldClazz);

            this.caller = caller;
        }

        public T swapCaller(T caller) {
            T old = this.caller;
            this.caller = caller;
            return old;
        }
        
        public static class Safe<T, E> extends Instance<T, E> {
            public Safe(T caller, String fieldName, Class<E> fieldClazz) {
                super(caller, fieldName, fieldClazz);
            }
            
            public Uncertain<E> getValue() {
                return getValue(this.caller);
            }

            public Uncertain<E> getValue(T caller) {
                if (cachedField == null) {
                    var field = ReflectionUtil.findFieldSafe(clazz, fieldName);

                    if (field.isMissing()) {
                        return Uncertain.empty();
                    }

                    cachedField = field.get();
                }

                return ReflectionUtil.getValueForFieldSafe(caller, cachedField, fieldClazz, true);
            }
        }

        public static class Unsafe<T, E> extends Instance<T, E> {
            public Unsafe(T caller, String fieldName, Class<E> fieldClazz) {
                super(caller, fieldName, fieldClazz);
            }
            
            public E getValue() {
                return getValue(this.caller);
            }
            
            public E getValue(T caller) {
                if (cachedField == null) {
                    cachedField = ReflectionUtil.findField(clazz, fieldName);
                }

                return ReflectionUtil.getValueForField(caller, cachedField, fieldClazz, true);
            }
        }
    }

    public static abstract class Static<T, E> extends FieldJailbreak<T, E> {
        protected Static(Class<T> clazz, String fieldName, Class<E> fieldClazz) {
            super(clazz, fieldName, fieldClazz);
        }

        public static class Safe<T, E> extends Static<T, E> {
            public Safe(Class<T> clazz, String fieldName, Class<E> fieldClazz) {
                super(clazz, fieldName, fieldClazz);
            }
            
            public Uncertain<E> getValue() {
                if (cachedField == null) {
                    var field = ReflectionUtil.findStaticFieldSafe(clazz, fieldName);

                    if (field.isMissing()) {
                        return Uncertain.empty();
                    }

                    cachedField = field.get();
                }
                
                return ReflectionUtil.getStaticValueForFieldSafe(cachedField, fieldClazz, true);
            }
        }

        public static class Unsafe<T, E> extends Static<T, E> {
            public Unsafe(Class<T> clazz, String fieldName, Class<E> fieldClazz) {
                super(clazz, fieldName, fieldClazz);
            }
            
            public E getValue() {
                if (cachedField == null) {
                    cachedField = ReflectionUtil.findStaticField(clazz, fieldName);
                }

                return ReflectionUtil.getStaticValueForField(cachedField, fieldClazz, true);
            }
        }
    }
}
