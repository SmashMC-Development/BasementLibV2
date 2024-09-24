package dev.notkili.basementdev.basementlib.core.util.reflection.jailbreak;

import dev.notkili.basementdev.basementlib.core.util.reflection.MethodParameters;
import dev.notkili.basementdev.basementlib.core.util.reflection.jailbreak.impl.*;
import lombok.NonNull;

public class Jailbreak<T> {
    public static <T> InstanceJailbreak.Safe<T> safe(@NonNull T object) {
        return new InstanceJailbreak.Safe<>(object);
    }
    public static <T> InstanceJailbreak.Safe<T> safe(@NonNull T object, @NonNull Class<T> clazz) {
        return new InstanceJailbreak.Safe<>(object, clazz);
    }
    public static <T> ClassJailbreak.Safe<T> safe(@NonNull Class<T> clazz) {
        return new ClassJailbreak.Safe<>(clazz);
    }
    public static <T> ConstructorJailbreak.Safe<T> safe(@NonNull Class<T> clazz, @NonNull Class<?>... parameters) {
        return new ConstructorJailbreak.Safe<>(clazz, parameters);
    }
    public static <T> ConstructorJailbreak.Safe<T> safe(@NonNull Class<T> clazz, @NonNull MethodParameters parameters) {
        return new ConstructorJailbreak.Safe<>(clazz, parameters);
    }
    public static <T, E> MethodJailbreak.Instance.Safe<T, E> safe(@NonNull T caller, @NonNull String methodName, @NonNull Class<E> resultClazz, @NonNull Class<?>... parameters) {
        return new MethodJailbreak.Instance.Safe<>(caller, methodName, resultClazz, parameters);
    }
    public static <T, E> MethodJailbreak.Instance.Safe<T, E> safe(@NonNull T caller, @NonNull String methodName, @NonNull Class<E> resultClazz, @NonNull MethodParameters parameters) {
        return new MethodJailbreak.Instance.Safe<>(caller, methodName, resultClazz, parameters);
    }
    public static <T, E> MethodJailbreak.Static.Safe<T, E> safe(@NonNull Class<T> clazz, @NonNull String methodName, @NonNull Class<E> resultClazz, @NonNull Class<?>... parameters) {
        return new MethodJailbreak.Static.Safe<>(clazz, methodName, resultClazz, parameters);
    }
    public static <T, E> MethodJailbreak.Static.Safe<T, E> safe(@NonNull Class<T> clazz, @NonNull String methodName, @NonNull Class<E> resultClazz, @NonNull MethodParameters parameters) {
        return new MethodJailbreak.Static.Safe<>(clazz, methodName, resultClazz, parameters);
    }
    public static <T, E> FieldJailbreak.Instance.Safe<T, E> safe(@NonNull T caller, @NonNull String fieldName, @NonNull Class<E> fieldClazz) {
        return new FieldJailbreak.Instance.Safe<>(caller, fieldName, fieldClazz);
    }
    public static <T, E> FieldJailbreak.Static.Safe<T, E> safe(@NonNull Class<T> clazz, @NonNull String fieldName, @NonNull Class<E> fieldClazz) {
        return new FieldJailbreak.Static.Safe<>(clazz, fieldName, fieldClazz);
    }
    
    public static <T> InstanceJailbreak.Unsafe<T> unsafe(T object) {
        return new InstanceJailbreak.Unsafe<>(object);
    }
    public static <T> InstanceJailbreak.Unsafe<T> unsafe(T object, Class<T> clazz) {
        return new InstanceJailbreak.Unsafe<>(object, clazz);
    }
    public static <T>ClassJailbreak.Unsafe<T> unsafe(Class<T> clazz) {
        return new ClassJailbreak.Unsafe<>(clazz);
    }
    public static <T> ConstructorJailbreak.Unsafe<T> unsafe(Class<T> clazz, Class<?>... parameters) {
        return new ConstructorJailbreak.Unsafe<>(clazz, parameters);
    }
    public static <T> ConstructorJailbreak.Unsafe<T> unsafe(Class<T> clazz, MethodParameters parameters) {
        return new ConstructorJailbreak.Unsafe<>(clazz, parameters);
    }
    public static <T, E> MethodJailbreak.Instance.Unsafe<T, E> unsafe(T caller, String methodName, Class<E> resultClazz, Class<?>... parameters) {
        return new MethodJailbreak.Instance.Unsafe<>(caller, methodName, resultClazz, parameters);
    }
    public static <T, E> MethodJailbreak.Instance.Unsafe<T, E> unsafe(T caller, String methodName, Class<E> resultClazz, MethodParameters parameters) {
        return new MethodJailbreak.Instance.Unsafe<>(caller, methodName, resultClazz, parameters);
    }
    public static <T, E> MethodJailbreak.Static.Unsafe<T, E> unsafe(Class<T> clazz, String methodName, Class<E> resultClazz, Class<?>... parameters) {
        return new MethodJailbreak.Static.Unsafe<>(clazz, methodName, resultClazz, parameters);
    }
    public static <T, E> MethodJailbreak.Static.Unsafe<T, E> unsafe(Class<T> clazz, String methodName, Class<E> resultClazz, MethodParameters parameters) {
        return new MethodJailbreak.Static.Unsafe<>(clazz, methodName, resultClazz, parameters);
    }
    public static <T, E> FieldJailbreak.Instance.Unsafe<T, E> unsafe(T caller, String fieldName, Class<E> fieldClazz) {
        return new FieldJailbreak.Instance.Unsafe<>(caller, fieldName, fieldClazz);
    }
    public static <T, E> FieldJailbreak.Static.Unsafe<T, E> unsafe(Class<T> clazz, String fieldName, Class<E> fieldClazz) {
        return new FieldJailbreak.Static.Unsafe<>(clazz, fieldName, fieldClazz);
    }
    
}
