package dev.notkili.basementdev.basementlib.core.util.reflection.jailbreak.impl;

import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;
import dev.notkili.basementdev.basementlib.core.util.reflection.MethodArguments;
import dev.notkili.basementdev.basementlib.core.util.reflection.MethodParameters;

import dev.notkili.basementdev.basementlib.core.util.reflection.ReflectionUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

public abstract class MethodJailbreak<T, E> {
    protected final Class<T> clazz;
    protected final String methodName;
    protected final Class<E> resultClazz;
    protected final MethodParameters parameters;

    protected Method cachedMethod = null;

    protected MethodJailbreak(Class<T> clazz, String methodName, Class<E> resultClazz, Class<?>... parameters) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.resultClazz = resultClazz;
        this.parameters = new MethodParameters(parameters);
    }

    protected MethodJailbreak(Class<T> clazz, String methodName, Class<E> resultClazz, MethodParameters parameters) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.resultClazz = resultClazz;
        this.parameters = parameters;
    }

    @Getter
    @Setter
    public static abstract class Instance<T, E> extends MethodJailbreak<T, E> {
        protected T caller;
        
        @SuppressWarnings("unchecked")
        protected Instance(T caller, String methodName, Class<E> resultClazz, Class<?>... parameters) {
            super((Class<T>) caller.getClass(), methodName, resultClazz, parameters);
            
            this.caller = caller;
        }

        @SuppressWarnings("unchecked")
        protected Instance(T caller, String methodName, Class<E> resultClazz, MethodParameters parameters) {
            super((Class<T>) caller.getClass(), methodName, resultClazz, parameters);
            
            this.caller = caller;
        }
        
        public T swapCaller(T caller) {
            T old = this.caller;
            this.caller = caller;
            return old;
        }

        public static class Safe<T, E> extends Instance<T, E> {
            public Safe(T caller, String methodName, Class<E> resultClazz, Class<?>... parameters) {
                super(caller, methodName, resultClazz, parameters);
            }

            public Safe(T caller, String methodName, Class<E> resultClazz, MethodParameters parameters) {
                super(caller, methodName, resultClazz, parameters);
            }
            
            public Uncertain<E> invoke(Object... arguments) {
                return invoke(this.caller, arguments);
            }
            
            public Uncertain<E> invoke(T caller, Object... arguments) {
                if (cachedMethod == null) {
                    var method = ReflectionUtil.findMethodSafe(clazz, methodName, parameters);
                    
                    if (method.isMissing()) {
                        return Uncertain.empty();
                    }
                    
                    cachedMethod = method.get();
                }
                
                return ReflectionUtil.invokeMethodSafe(caller, cachedMethod, resultClazz, new MethodArguments(arguments), true);
            }
        }
        
        public static class Unsafe<T, E> extends Instance<T, E> {
            public Unsafe(T caller, String methodName, Class<E> resultClazz, Class<?>... parameters) {
                super(caller, methodName, resultClazz, parameters);
            }

            public Unsafe(T caller, String methodName, Class<E> resultClazz, MethodParameters parameters) {
                super(caller, methodName, resultClazz, parameters);
            }
            
            public E invoke(Object... arguments) {
                return invoke(this.caller, arguments);
            }
            
            public E invoke(T caller, Object... arguments) {
                if (cachedMethod == null) {
                    cachedMethod = ReflectionUtil.findMethod(clazz, methodName, parameters);
                }
                
                return ReflectionUtil.invokeMethod(caller, cachedMethod, resultClazz, new MethodArguments(arguments), true);
            }
        }
    }
    
    public static abstract class Static<T, E> extends MethodJailbreak<T, E> {
        protected Static(Class<T> clazz, String methodName, Class<E> resultClazz, Class<?>... parameters) {
            super(clazz, methodName, resultClazz, parameters);
        }

        protected Static(Class<T> clazz, String methodName, Class<E> resultClazz, MethodParameters parameters) {
            super(clazz, methodName, resultClazz, parameters);
        }

        public static class Safe<T, E> extends Static<T, E> {
            public Safe(Class<T> clazz, String methodName, Class<E> resultClazz, Class<?>... parameters) {
                super(clazz, methodName, resultClazz, parameters);
            }

            public Safe(Class<T> clazz, String methodName, Class<E> resultClazz, MethodParameters parameters) {
                super(clazz, methodName, resultClazz, parameters);
            }
            
            public Uncertain<E> invoke(Object... arguments) {
                if (cachedMethod == null) {
                    var method = ReflectionUtil.findStaticMethodSafe(clazz, methodName, parameters);
                    
                    if (method.isMissing()) {
                        return Uncertain.empty();
                    }
                    
                    cachedMethod = method.get();
                }
                
                return ReflectionUtil.invokeStaticMethodSafe(cachedMethod, resultClazz, new MethodArguments(arguments), true);
            }
        }

        public static class Unsafe<T, E> extends Static<T, E> {
            public Unsafe(Class<T> clazz, String methodName, Class<E> resultClazz, Class<?>... parameters) {
                super(clazz, methodName, resultClazz, parameters);
            }

            public Unsafe(Class<T> clazz, String methodName, Class<E> resultClazz, MethodParameters parameters) {
                super(clazz, methodName, resultClazz, parameters);
            }
            
            public E invoke(Object... arguments) {
                if (cachedMethod == null) {
                    cachedMethod = ReflectionUtil.findStaticMethod(clazz, methodName, parameters);
                }
                
                return ReflectionUtil.invokeStaticMethod(cachedMethod, resultClazz, new MethodArguments(arguments), true);
            }
        }
    }
}
