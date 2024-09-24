package dev.notkili.basementdev.basementlib.core.util.reflection;

import dev.notkili.basementdev.basementlib.core.util.misc.Uncertain;
import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

public class ReflectionUtil {
    private static final String ERROR_PREFIX = "REFLECTION FAILED >>";

    /**
     * Retrieves the value of a field from the provided declaring object using reflection.
     * Finds the field by name using the {@link #findField(Class, String)} method and delegates the result to the {@link #getValueForField(E, Field, Class, boolean)} method.
     * @param declaringObject The object containing the field
     * @param field The name of the field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return The value of the field
     * @throws ReflectionException If the field cannot be accessed
     * @see #getValueForField(E, Field, Class, boolean)
     * @author NotKili
     */
    public static <T, E> T getValueForField(@NonNull E declaringObject, String field, Class<T> fieldClazz, boolean forceAccess) {
        return getValueForField(declaringObject, findField(declaringObject.getClass(), field), fieldClazz, forceAccess);
    }

    /**
     * Retrieves the value of a field from the provided declaring object using reflection.
     * Checks the field validity according to {@link #checkField(Class, Field, Class)}, optionally forces access, and casts the field value to the specified type.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return The value of the field
     * @throws ReflectionException If the field cannot be accessed
     * @author NotKili
     */
    public static <T, E> T getValueForField(@NonNull E declaringObject, Field reflectedField, Class<T> fieldClazz, boolean forceAccess) {
        checkField(declaringObject.getClass(), reflectedField, fieldClazz);

        if (forceAccess) reflectedField.setAccessible(true);

        try {
            return fieldClazz.cast(reflectedField.get(declaringObject));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access field '" + reflectedField.getName() + "' within class '" + declaringObject.getClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely retrieves the value of a field from the provided declaring object using reflection.
     * Finds the field by name using the {@link #findFieldSafe(Class, String)} method, then delegates the result to the {@link #getValueForFieldSafe(E, Field, Class, boolean)} method.
     * Returns an {@link Uncertain} wrapper to indicate the presence or absence of the field value.
     * @param declaringObject The object containing the field
     * @param field The name of the field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the field value or empty if the field is not found
     * @see #getValueForFieldSafe(E, Field, Class, boolean)
     * @author NotKili
     */
    public static <T, E> @NonNull Uncertain<T> getValueForFieldSafe(@NonNull E declaringObject, String field, Class<T> fieldClazz, boolean forceAccess) {
        return findFieldSafe(declaringObject.getClass(), field).ifPresentOrElseReturn(reflectedField -> getValueForFieldSafe(declaringObject, reflectedField, fieldClazz, forceAccess), Uncertain::empty);
    }

    /**
     * Safely retrieves the value of a field from the provided declaring object using reflection.
     * Checks the validity of the field according to {@link #checkField(Class, Field, Class)}, optionally forces access, and casts the field value to the specified type.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field value, and to manage exceptions.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the field value or empty if an exception occurs
     * @author NotKili
     */
    public static <T, E> @NonNull Uncertain<T> getValueForFieldSafe(@NonNull E declaringObject, Field reflectedField, Class<T> fieldClazz, boolean forceAccess) {
        try {
            checkField(declaringObject.getClass(), reflectedField, fieldClazz);

            if (forceAccess) reflectedField.setAccessible(true);

            return Uncertain.of(fieldClazz.cast(reflectedField.get(declaringObject)));
        } catch (ReflectionException | IllegalAccessException | IllegalArgumentException | ClassCastException e) {
            return Uncertain.empty();
        }
    }

    /**
     * Retrieves the static value of a field from the specified class using reflection.
     * Finds the field by name using the {@link #findFieldSafe(Class, String)} method and delegates the result to the {@link #getStaticValueForField(Field, Class, boolean)} method.
     * @param declaringClazz The {@link Class} object representing the class containing the static field
     * @param field The name of the static field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @return The static value of the field
     * @throws ReflectionException If the static field cannot be accessed
     * @see #getStaticValueForField(Field, Class, boolean)
     * @author NotKili
     */
    public static <T, E> T getStaticValueForField(Class<E> declaringClazz, String field, Class<T> fieldClazz, boolean forceAccess) {
        return getStaticValueForField(findStaticField(declaringClazz, field), fieldClazz, forceAccess);
    }

    /**
     * Retrieves the static value of a field from the provided {@link Field} object using reflection.
     * Checks the field's validity, optionally forces access, and casts the field value to the specified type.
     * @param reflectedField The {@link Field} object representing the static field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @return The static value of the field
     * @throws ReflectionException If the static field cannot be accessed
     * @author NotKili
     */
    public static <T> T getStaticValueForField(Field reflectedField, Class<T> fieldClazz, boolean forceAccess) {
        checkStaticField(reflectedField.getDeclaringClass(), reflectedField, fieldClazz);

        if (forceAccess) reflectedField.setAccessible(true);

        try {
            return fieldClazz.cast(reflectedField.get(null));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access static field '" + reflectedField.getName() + "' within class '" + reflectedField.getDeclaringClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely retrieves the static value of a field from the specified class using reflection.
     * Finds the field by name using the {@link #findStaticFieldSafe(Class, String)} method, then delegates the result to the {@link #getStaticValueForFieldSafe(Field, Class, boolean)} method.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field value, and to manage exceptions.
     * @param declaringClazz The {@link Class} object representing the class containing the static field
     * @param field The name of the static field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @return An {@link Uncertain} containing the static field value or empty if the field is not found
     * @see #getStaticValueForFieldSafe(Field, Class, boolean)
     * @author NotKili
     */
    public static <T, E> @NonNull Uncertain<T> getStaticValueForFieldSafe(Class<E> declaringClazz, String field, Class<T> fieldClazz, boolean forceAccess) {
        return findStaticFieldSafe(declaringClazz, field).ifPresentOrElseReturn(reflectedField -> getStaticValueForFieldSafe(reflectedField, fieldClazz, forceAccess), Uncertain::empty);
    }

    /**
     * Safely retrieves the static value of a field from the provided {@link Field} object using reflection.
     * Handles exceptions, static fields, and access control, returning the value wrapped in an {@link Uncertain}.
     * @param reflectedField The {@link Field} object representing the static field to retrieve
     * @param fieldClazz The {@link Class} object representing the type of the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @return An {@link Uncertain} containing the static field value or empty if an exception occurs
     * @author NotKili
     */
    public static <T> @NonNull Uncertain<T> getStaticValueForFieldSafe(Field reflectedField, Class<T> fieldClazz, boolean forceAccess) {
        try {
            checkStaticField(reflectedField.getDeclaringClass(), reflectedField, fieldClazz);

            if (forceAccess) reflectedField.setAccessible(true);

            return Uncertain.of(fieldClazz.cast(reflectedField.get(null)));
        } catch (IllegalAccessException | IllegalArgumentException | ClassCastException e) {
            return Uncertain.empty();
        }
    }

    /**
     * Sets the value of a field in the provided declaring object using reflection.
     * Finds the field by name using the {@link #findField(Class, String)} method and delegates the result to the {@link #setValueOfField(E, Field, Object, boolean)} method.
     * @param declaringObject The object containing the field
     * @param field The name of the field to set
     * @param value The value to set in the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @throws ReflectionException If the field cannot be accessed
     * @see #setValueOfField(E, Field, Object, boolean)
     * @author NotKili
     */
    public static <T, E> void setValueOfField(@NonNull E declaringObject, String field, T value, boolean forceAccess) {
        setValueOfField(declaringObject, findField(declaringObject.getClass(), field), value, forceAccess);
    }

    /**
     * Sets the value of a field in the provided declaring object using reflection.
     * Checks the field's validity according to {@link #checkField(Class, Field, Class)}, optionally forces access, and sets the field value.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to set
     * @param value The value to set in the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @throws ReflectionException If the field cannot be accessed or the check fails
     */
    public static <T, E> void setValueOfField(@NonNull E declaringObject, Field reflectedField, T value, boolean forceAccess) {
        checkField(declaringObject.getClass(), reflectedField, value.getClass());

        if (forceAccess) reflectedField.setAccessible(true);

        try {
            reflectedField.set(declaringObject, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access field '" + reflectedField.getName() + "' within class '" + declaringObject.getClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely sets the value of a field in the provided declaring object using reflection.
     * Finds the field by name using the {@link #findFieldSafe(Class, String)} method, then delegates the result to the {@link #setValueOfFieldSafe(E, Field, Object, boolean)} method.
     * Returns true if the field was successfully set, otherwise false.
     * @param declaringObject The object containing the field
     * @param field The name of the field to set
     * @param value The value to set in the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return true if the field was successfully set, false otherwise
     * @see #setValueOfFieldSafe(E, Field, Object, boolean)
     * @author NotKili
     */
    public static <T, E> boolean setValueOfFieldSafe(@NonNull E declaringObject, String field, T value, boolean forceAccess) {
        return findFieldSafe(declaringObject.getClass(), field).ifPresentOrElseReturn(reflectedField -> setValueOfFieldSafe(declaringObject, reflectedField, value, forceAccess), () -> false);
    }

    /**
     * Safely sets the value of a field in the provided declaring object using reflection.
     * Checks the field's validity according to {@link #checkField(Class, Field, Class)}, optionally forces access, and sets the field value. Returns true if successful, otherwise false.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to set
     * @param value The value to set in the field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return true if the field was successfully set, false otherwise
     * @author NotKili
     */
    public static <T, E> boolean setValueOfFieldSafe(@NonNull E declaringObject, Field reflectedField, T value, boolean forceAccess) {
        try {
            checkField(declaringObject.getClass(), reflectedField, value.getClass());

            if (forceAccess) reflectedField.setAccessible(true);

            reflectedField.set(declaringObject, value);
            return true;
        } catch (ReflectionException | IllegalAccessException | ClassCastException e) {
            return false;
        }
    }

    /**
     * Sets the static value of a field in the specified class using reflection.
     * Finds the field by name using the {@link #findStaticField(Class, String)} method and delegates the result to the {@link #setStaticValueOfField(Field, Object, boolean)} method.
     * @param declaringClazz The class containing the static field
     * @param field The name of the static field to set
     * @param value The value to set in the static field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @throws ReflectionException If the field cannot be accessed
     * @see #setStaticValueOfField(Field, Object, boolean)
     * @author NotKili
     */
    public static <T, E> void setStaticValueOfField(Class<E> declaringClazz, String field, T value, boolean forceAccess) {
        setStaticValueOfField(findStaticField(declaringClazz, field), value, forceAccess);
    }

    /**
     * Sets the static value of a field in the specified class using reflection.
     * Checks the field's validity according to {@link #checkStaticField(Class, Field, Class)}, optionally forces access, and sets the static field value.
     * @param reflectedField The {@link Field} object representing the static field to set
     * @param value The value to set in the static field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @throws ReflectionException If the field cannot be accessed or the check fails
     */
    public static <T> void setStaticValueOfField(Field reflectedField, T value, boolean forceAccess) {
        checkStaticField(reflectedField.getDeclaringClass(), reflectedField, value.getClass());

        try {
            if (forceAccess) reflectedField.setAccessible(true);
            
            reflectedField.set(null, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access static field '" + reflectedField.getName() + "' within class '" + reflectedField.getDeclaringClass().getSimpleName() + "'", e);
        }
    }
    
    /**
     * Safely sets the static value of a field in the specified class using reflection.
     * Finds the field by name using the {@link #findStaticFieldSafe(Class, String)} method, then delegates the result to the {@link #setStaticValueOfFieldSafe(Field, Object, boolean)} method.
     * Returns true if the field was successfully set, otherwise false.
     * @param declaringClazz The class containing the static field
     * @param field The name of the static field to set
     * @param value The value to set in the static field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @return true if the field was successfully set, false otherwise
     * @see #setStaticValueOfFieldSafe(Field, Object, boolean)
     */
    public static <T, E> boolean setStaticValueOfFieldSafe(Class<E> declaringClazz, String field, T value, boolean forceAccess) {
        return findStaticFieldSafe(declaringClazz, field).ifPresentOrElseReturn(reflectedField -> setStaticValueOfFieldSafe(reflectedField, value, forceAccess), () -> false);
    }

    /**
     * Safely sets the static value of a field in the specified class using reflection.
     * Checks the field's validity according to {@link #checkStaticField(Class, Field, Class)}, optionally forces access, and sets the static field value. Returns true if successful, otherwise false.
     * @param reflectedField The {@link Field} object representing the static field to set
     * @param value The value to set in the static field
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @return true if the field was successfully set, false otherwise
     */
    public static <T> boolean setStaticValueOfFieldSafe(Field reflectedField, T value, boolean forceAccess) {
        try {
            checkStaticField(reflectedField.getDeclaringClass(), reflectedField, value.getClass());

            if (forceAccess) reflectedField.setAccessible(true);

            reflectedField.set(null, value);
            return true;
        } catch (ReflectionException | IllegalAccessException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Modifies the value of a field in the specified object using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findField(Class, String)} method and delegates the result to the {@link #modifyValueOfField(Object, Field, Class, Function, boolean)} method.
     * @param declaringObject The object containing the field
     * @param field The name of the field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @throws ReflectionException If the field cannot be accessed
     * @see #modifyValueOfField(Object, Field, Class, Function, boolean)
     * @author NotKili
     */
    public static <T, E> void modifyValueOfField(@NonNull E declaringObject, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        modifyValueOfField(declaringObject, findField(declaringObject.getClass(), field), fieldClazz, modifier, forceAccess);
    }

    /**
     * Modifies the value of a field in the specified object using a provided {@link Function}.
     * Checks the field's validity according to {@link #checkField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @throws ReflectionException If the field cannot be accessed or the check fails
     * @author NotKili
     */
    public static <T, E> void modifyValueOfField(@NonNull E declaringObject, Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        checkField(declaringObject.getClass(), reflectedField, fieldClazz);

        if (forceAccess) reflectedField.setAccessible(true);

        try {
            reflectedField.set(declaringObject, modifier.apply(fieldClazz.cast(reflectedField.get(declaringObject))));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access field '" + reflectedField.getName() + "' within class '" + declaringObject.getClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely modifies the value of a field in the specified object using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findFieldSafe(Class, String)} method, then delegates the result to the {@link #modifyValueOfFieldSafe(Object, Field, Class, Function, boolean)} method.
     * Returns true if the field was successfully modified, otherwise false.
     * @param declaringObject The object containing the field
     * @param field The name of the field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return true if the field was successfully modified, false otherwise
     * @see #modifyValueOfFieldSafe(Object, Field, Class, Function, boolean)
     * @author NotKili
     */
    public static <T, E> boolean modifyValueOfFieldSafe(@NonNull E declaringObject, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        return findFieldSafe(declaringObject.getClass(), field).ifPresentOrElseReturn(reflectedField -> modifyValueOfFieldSafe(declaringObject, reflectedField, fieldClazz, modifier, forceAccess), () -> false);
    }
    
    /**
     * Safely modifies the value of a field in the specified object using a provided {@link Function}.
     * Checks the field's validity according to {@link #checkField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * Returns true if the field was successfully modified, otherwise false.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return true if the field was successfully modified, false otherwise
     */
    public static <T, E> boolean modifyValueOfFieldSafe(@NonNull E declaringObject, Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        try {
            checkField(declaringObject.getClass(), reflectedField, fieldClazz);

            if (forceAccess) reflectedField.setAccessible(true);

            reflectedField.set(declaringObject, modifier.apply(fieldClazz.cast(reflectedField.get(declaringObject))));
            return true;
        } catch (ReflectionException | IllegalAccessException | IllegalArgumentException | ClassCastException e) {
            return false;
        }
    }

    /**
     * Modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findStaticField(Class, String)} method and delegates the result to the {@link #modifyStaticValueOfField(Field, Class, Function, boolean)} method.
     * @param declaringClazz The class containing the static field
     * @param field The name of the static field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @throws ReflectionException If the field cannot be accessed
     * @see #modifyStaticValueOfField(Field, Class, Function, boolean)
     * @author NotKili
     */
    public static <T, E> void modifyStaticValueOfField(Class<E> declaringClazz, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        modifyStaticValueOfField(findStaticField(declaringClazz, field), fieldClazz, modifier, forceAccess);
    }

    /**
     * Modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Checks the field's validity according to {@link #checkStaticField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * @param reflectedField The {@link Field} object representing the static field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @throws ReflectionException If the field cannot be accessed or the check fails
     */
    public static <T> void modifyStaticValueOfField(Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        checkStaticField(reflectedField.getDeclaringClass(), reflectedField, fieldClazz);

        if (forceAccess) reflectedField.setAccessible(true);

        try {
            reflectedField.set(null, modifier.apply(fieldClazz.cast(reflectedField.get(null))));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access static field '" + reflectedField.getName() + "' within class '" + reflectedField.getDeclaringClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findStaticFieldSafe(Class, String)} method, then delegates the result to the {@link #modifyStaticValueOfFieldSafe(Field, Class, Function, boolean)} method.
     * Returns true if the field was successfully modified, otherwise false.
     * @param declaringClazz The class containing the static field
     * @param field The name of the static field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @return true if the field was successfully modified, false otherwise
     * @see #modifyStaticValueOfFieldSafe(Field, Class, Function, boolean)
     */
    public static <T, E> boolean modifyStaticValueOfFieldSafe(Class<E> declaringClazz, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        return findStaticFieldSafe(declaringClazz, field).ifPresentOrElseReturn(reflectedField -> modifyStaticValueOfFieldSafe(reflectedField, fieldClazz, modifier, forceAccess), () -> false);
    }

    /**
     * Safely modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Checks the field's validity according to {@link #checkStaticField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * Returns true if the field was successfully modified, otherwise false.
     * @param reflectedField The {@link Field} object representing the static field to modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @return true if the field was successfully modified, false otherwise
     */
    public static <T> boolean modifyStaticValueOfFieldSafe(Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        try {
            checkStaticField(reflectedField.getDeclaringClass(), reflectedField, fieldClazz);

            if (forceAccess) reflectedField.setAccessible(true);

            reflectedField.set(null, modifier.apply(fieldClazz.cast(reflectedField.get(null))));
            return true;
        } catch (ReflectionException | IllegalAccessException | IllegalArgumentException | ClassCastException e) {
            return false;
        }
    }

    /**
     * Retrieves and modifies the value of a field in the specified object using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findField(Class, String)} method and delegates the result to the {@link #getAndModifyValueOfField(Object, Field, Class, Function, boolean)} method.
     * @param declaringObject The object containing the field
     * @param field The name of the field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return The original value of the field before modification
     * @throws ReflectionException If the field cannot be accessed
     * @see #getAndModifyValueOfField(Object, Field, Class, Function, boolean)
     * @author NotKili
     */
    public static <T, E> T getAndModifyValueOfField(@NonNull E declaringObject, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        return getAndModifyValueOfField(declaringObject, findField(declaringObject.getClass(), field), fieldClazz, modifier, forceAccess);
    }

    /**
     * Retrieves and modifies the value of a field in the specified object using a provided {@link Function} as modifier.
     * Checks the field's validity according to {@link #checkField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return The original value of the field before modification
     * @throws ReflectionException If the field cannot be accessed or the check fails
     * @author NotKili
     */
    public static <T, E> T getAndModifyValueOfField(@NonNull E declaringObject, Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        checkField(declaringObject.getClass(), reflectedField, fieldClazz);

        try {
            if (forceAccess) reflectedField.setAccessible(true);
            
            T current = fieldClazz.cast(reflectedField.get(declaringObject));
            reflectedField.set(declaringObject, modifier.apply(current));
            return current;
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access field '" + reflectedField.getName() + "' within class '" + declaringObject.getClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely retrieves and modifies the value of a field in the specified object using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findFieldSafe(Class, String)} method, then delegates the result to the {@link #getAndModifyValueOfFieldSafe(Object, Field, Class, Function, boolean)} method.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field value, and to manage exceptions.
     * @param declaringObject The object containing the field
     * @param field The name of the field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the original value of the field before modification or empty if the field is not found
     * @see #getAndModifyValueOfFieldSafe(Object, Field, Class, Function, boolean) (Object, Field, Class, Function, boolean)
     * @author NotKili
     */
    public static <T, E> @NonNull Uncertain<T> getAndModifyValueOfFieldSafe(@NonNull E declaringObject, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        return findFieldSafe(declaringObject.getClass(), field).ifPresentOrElseReturn(reflectedField -> getAndModifyValueOfFieldSafe(declaringObject, reflectedField, fieldClazz, modifier, forceAccess), Uncertain::empty);
    }
    
    /**
     * Safely retrieves and modifies the value of a field in the specified object using a provided {@link Function} as modifier.
     * Checks the field's validity according to {@link #checkField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field value, and to manage exceptions.
     * @param declaringObject The object containing the field
     * @param reflectedField The {@link Field} object representing the field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the original value of the field before modification or empty if an exception occurs
     * @author NotKili
     */
    public static <T, E> @NonNull Uncertain<T> getAndModifyValueOfFieldSafe(@NonNull E declaringObject, Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        try {
            checkField(declaringObject.getClass(), reflectedField, fieldClazz);

            if (forceAccess) reflectedField.setAccessible(true);

            T current = fieldClazz.cast(reflectedField.get(declaringObject));
            reflectedField.set(declaringObject, modifier.apply(current));
            return Uncertain.of(current);
        } catch (ReflectionException | IllegalAccessException | IllegalArgumentException | ClassCastException e) {
            return Uncertain.empty();
        }
    }
    
    /**
     * Retrieves and modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findStaticField(Class, String)} method and delegates the result to the {@link #getAndModifyStaticValueOfField(Field, Class, Function, boolean)} method.
     * @param declaringClazz The class containing the static field
     * @param field The name of the static field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @return The original value of the static field before modification
     * @throws ReflectionException If the field cannot be accessed
     * @see #getAndModifyStaticValueOfField(Field, Class, Function, boolean)
     * @author NotKili
     */
    public static <T, E> T getAndModifyStaticValueOfField(Class<E> declaringClazz, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        return getAndModifyStaticValueOfField(findStaticField(declaringClazz, field), fieldClazz, modifier, forceAccess);
    }
    
    /**
     * Retrieves and modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Checks the field's validity according to {@link #checkStaticField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * @param reflectedField The {@link Field} object representing the static field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @return The original value of the static field before modification
     * @throws ReflectionException If the field cannot be accessed or the check fails
     * @author NotKili
     */
    public static <T> T getAndModifyStaticValueOfField(Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        checkStaticField(reflectedField.getDeclaringClass(), reflectedField, fieldClazz);

        if (forceAccess) reflectedField.setAccessible(true);

        try {
            T current = fieldClazz.cast(reflectedField.get(null));
            reflectedField.set(null, modifier.apply(current));
            return current;
        } catch (IllegalAccessException e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not access static field '" + reflectedField.getName() + "' within class '" + reflectedField.getDeclaringClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely retrieves and modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Finds the field by name using the {@link #findStaticFieldSafe(Class, String)} method, then delegates the result to the {@link #getAndModifyStaticValueOfFieldSafe(Class, String, Class, Function, boolean)} method.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field value, and to manage exceptions.
     * @param declaringClazz The class containing the static field
     * @param field The name of the static field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @return An {@link Uncertain} containing the original value of the static field before modification or empty if the field is not found
     * @see #getAndModifyStaticValueOfFieldSafe(Field, Class, Function, boolean)
     * @author NotKili
     */
    public static <T, E> @NonNull Uncertain<T> getAndModifyStaticValueOfFieldSafe(Class<E> declaringClazz, String field, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        return findStaticFieldSafe(declaringClazz, field).ifPresentOrElseReturn(reflectedField -> getAndModifyStaticValueOfFieldSafe(reflectedField, fieldClazz, modifier, forceAccess), Uncertain::empty);

    }

    /**
     * Safely retrieves and modifies the static value of a field in the specified class using a provided {@link Function} as modifier.
     * Checks the field's validity according to {@link #checkStaticField(Class, Field, Class)}, optionally forces access, retrieves the current value, applies the modifier, and sets the modified value.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field value, and to manage exceptions.
     * @param reflectedField The {@link Field} object representing the static field to retrieve and modify
     * @param fieldClazz The {@link Class} of the field value type
     * @param modifier A {@link Function} that modifies the current field value
     * @param forceAccess If true, forces access to the field
     * @param <T> The type of the field value
     * @return An {@link Uncertain} containing the original value of the static field before modification or empty if an exception occurs
     * @author NotKili
     */
    public static <T> @NonNull Uncertain<T> getAndModifyStaticValueOfFieldSafe(Field reflectedField, Class<T> fieldClazz, Function<T, T> modifier, boolean forceAccess) {
        try {
            checkStaticField(reflectedField.getDeclaringClass(), reflectedField, fieldClazz);

            if (forceAccess) reflectedField.setAccessible(true);

            T current = fieldClazz.cast(reflectedField.get(null));
            reflectedField.set(null, modifier.apply(current));
            return Uncertain.of(current);
        } catch (IllegalAccessException | IllegalArgumentException | ClassCastException e) {
            return Uncertain.empty();
        }
    }

    /**
     * Finds a field by name in the specified class or its superclasses.
     * Searches the class hierarchy starting from the provided class, returning the {@link Field} object if found.
     * Throws a {@link ReflectionException} if the field cannot be located.
     * @param declaringClazz The class to search for the field
     * @param field The name of the field to find
     * @param <E> The type of the declaring class
     * @return The {@link Field} object representing the field
     * @throws ReflectionException If the field cannot be located
     * @author NotKili
     */
    public static <E> Field findField(@NonNull Class<E> declaringClazz, String field) {
        Class<?> tmpClazz = declaringClazz;

        while (tmpClazz != null) {
            try {
                return tmpClazz.getDeclaredField(field);
            } catch (Exception e) {
                tmpClazz = tmpClazz.getSuperclass();
            }
        }

        throw new ReflectionException(ERROR_PREFIX + "Could not locate field '" + field + "' within class '" + declaringClazz.getSimpleName() + "' or any of its Super-Classes");
    }

    /**
     * Safely finds a field by name in the specified class or its superclasses.
     * Searches the class hierarchy starting from the provided class, returning the {@link Field} object if found.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field, and to manage exceptions.
     * @param declaringClazz The class to search for the field
     * @param field The name of the field to find
     * @param <E> The type of the declaring class
     * @return An {@link Uncertain} containing the {@link Field} object representing the field or empty if the field is not found
     * @see #findField(Class, String)
     * @author NotKili
     */
    public static <E> Uncertain<Field> findFieldSafe(Class<E> declaringClazz, String field) {
        try {
            return Uncertain.of(findField(declaringClazz, field));
        } catch (ReflectionException e) {
            return Uncertain.empty();
        }
    }
    
    /**
     * Finds a static field by name in the specified class or its superclasses.
     * Searches the class hierarchy starting from the provided class, returning the {@link Field} object if found.
     * Throws a {@link ReflectionException} if the field cannot be located.
     * @param declaringClazz The class to search for the static field
     * @param field The name of the static field to find
     * @param <E> The type of the declaring class
     * @return The {@link Field} object representing the static field
     * @throws ReflectionException If the field cannot be located
     * @author NotKili
     */
    public static <E> Field findStaticField(Class<E> declaringClazz, String field) {
        Class<?> tmpClazz = declaringClazz;

        while (tmpClazz != null) {
            try {
                return tmpClazz.getDeclaredField(field);
            } catch (Exception e) {
                tmpClazz = tmpClazz.getSuperclass();
            }
        }

        throw new ReflectionException(ERROR_PREFIX + "Could not locate static field '" + field + "' within class '" + declaringClazz.getSimpleName() + "' or any of its Super-Classes");
    }

    /**
     * Safely finds a static field by name in the specified class or its superclasses.
     * Searches the class hierarchy starting from the provided class, returning the {@link Field} object if found.
     * Returns an {@link Uncertain} wrapper to handle the presence or absence of the field, and to manage exceptions.
     * @param declaringClazz The class to search for the static field
     * @param field The name of the static field to find
     * @param <E> The type of the declaring class
     * @return An {@link Uncertain} containing the {@link Field} object representing the static field or empty if the field is not found
     * @see #findStaticField(Class, String)
     * @author NotKili
     */
    public static <E> Uncertain<Field> findStaticFieldSafe(@NonNull Class<E> declaringClazz, String field) {
        try {
            return Uncertain.of(findStaticField(declaringClazz, field));
        } catch (ReflectionException e) {
            return Uncertain.empty();
        }
    }

    /**
     * Validates that the specified field matches the expected class and type constraints.
     * Checks if the field belongs to the declared class, is not static, and is of the correct type. Handles primitive type checks.
     * Throws a {@link ReflectionException} if any of the constraints are violated.
     * @param declaringClazz The class in which the field is declared
     * @param reflectedField The {@link Field} object to check
     * @param fieldClazz The expected {@link Class} of the field value type
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @throws ReflectionException If any validation check fails
     * @author NotKili
     */
    public static <T, E> void checkField(Class<E> declaringClazz, Field reflectedField, Class<T> fieldClazz) {
        String field = reflectedField.getName();

        if (!reflectedField.getDeclaringClass().isAssignableFrom(declaringClazz)) {
            throw new ReflectionException(ERROR_PREFIX + "Field '" + field + "' can not be accessed. Class '" + declaringClazz.getSimpleName() + "' is not assignable from '" + reflectedField.getDeclaringClass().getSimpleName() + "'");
        }

        if (Modifier.isStatic(reflectedField.getModifiers())) {
            throw new ReflectionException(ERROR_PREFIX + "Field '" + field + "' is STATIC in the context of class '" + reflectedField.getDeclaringClass().getSimpleName() + "'");
        }

        if (!fieldClazz.isAssignableFrom(reflectedField.getType())) {
            if (checkPrimitive(reflectedField, fieldClazz)) {
                return;
            }

            throw new ReflectionException(ERROR_PREFIX + "Field '" + field + "' is not of type '" + fieldClazz.getSimpleName() + "' in the context of class '" + reflectedField.getDeclaringClass().getSimpleName() + "'");
        }
    }

    /**
     * Validates that the specified static field meets the expected class and type constraints.
     * Checks if the field belongs to the declaring class, is static, and is of the correct type. Handles primitive type checks.
     * Throws a {@link ReflectionException} if any of the constraints are violated.
     * @param declaringClazz The class where the static field is declared
     * @param reflectedField The {@link Field} object to check
     * @param fieldClazz The expected {@link Class} of the field value type
     * @param <T> The type of the field value
     * @param <E> The type of the declaring class
     * @throws ReflectionException If any validation check fails
     * @author NotKili
     */
    public static <T, E> void checkStaticField(Class<E> declaringClazz, Field reflectedField, Class<T> fieldClazz) {
        String field = reflectedField.getName();
        
        if (!reflectedField.getDeclaringClass().isAssignableFrom(declaringClazz)) {
            throw new ReflectionException(ERROR_PREFIX + "Static Field '" + field + "' can not be accessed. Class '" + declaringClazz.getSimpleName() + "' is not assignable from '" + reflectedField.getDeclaringClass().getSimpleName() + "'");
        }

        if (!Modifier.isStatic(reflectedField.getModifiers())) {
            throw new ReflectionException(ERROR_PREFIX + "Field '" + field + "' is NOT STATIC in the context of class '" + reflectedField.getDeclaringClass().getSimpleName() + "'");
        }

        if (!fieldClazz.isAssignableFrom(reflectedField.getType())) {
            if (checkPrimitive(reflectedField, fieldClazz)) {
                return;
            }

            throw new ReflectionException(ERROR_PREFIX + "Static Field '" + field + "' is not of type '" + fieldClazz.getSimpleName() + "' in the context of class '" + reflectedField.getDeclaringClass().getSimpleName() + "'");
        }
    }

    /**
     * Checks if the {@link Field} represents a primitive type that can be converted to the specified class.
     * @param reflectedField The {@link Field} object to check
     * @param fieldClazz The {@link Class} of the field value type
     * @param <T> The type of the field value
     * @return {@code true} if the field is a primitive type and can be converted to the specified class, {@code false} otherwise
     * @see #canConvertFromPrimitive(Class, Class)
     * @author NotKili
     */
    public static <T> boolean checkPrimitive(Field reflectedField, Class<T> fieldClazz) {
        return reflectedField.getType().isPrimitive() && canConvertFromPrimitive(reflectedField.getType(), fieldClazz);
    }

    /**
     * Determines if a primitive type can be converted to the specified wrapper class.
     * @param primClass The primitive {@link Class} to check
     * @param other The target {@link Class} to convert to
     * @return {@code true} if the primitive type can be converted to the specified wrapper class, {@code false} otherwise
     */
    public static boolean canConvertFromPrimitive(Class<?> primClass, Class<?> other) {
        switch (primClass.getName()) {
            case "int":
                return other.equals(Integer.class);
            case "boolean":
                return other.equals(Boolean.class);
            case "long":
                return other.equals(Long.class);
            case "double":
                return other.equals(Double.class);
            case "char":
                return other.equals(Character.class);
            case "byte":
                return other.equals(Byte.class);
            case "float":
                return other.equals(Float.class);
            case "short":
                return other.equals(Short.class);
            default:
                return false;
        }
    }

    /**
     * Invokes a method on the specified object using the provided arguments.
     * Wraps the provided list of arguments into a {@link MethodArguments} object and delegates the result to the {@link #invokeMethod(Object, Method, Class, MethodArguments, boolean)} method.
     * @param declaringObject The object on which to invoke the method
     * @param methodName The name of the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return The result of the method invocation, cast to {@code valueClazz}
     * @throws ReflectionException If the method could not be invoked
     * @see #invokeMethod(Object, Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> T invokeMethod(E declaringObject, String methodName, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeMethod(declaringObject, methodName, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Invokes a method on the specified object using the provided method name and arguments.
     * This method finds the method using the {@link #findMethod(Class, String, MethodParameters)} method using the given name and argument types, and then delegates the result to the {@link #invokeMethod(Object, Method, Class, MethodArguments, boolean)} method.
     * @param declaringObject The object on which to invoke the method
     * @param methodName The name of the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return The result of the method invocation, cast to {@code valueClazz}
     * @throws ReflectionException If the method could not be invoked
     * @see #invokeMethod(Object, Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> T invokeMethod(E declaringObject, String methodName, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        return invokeMethod(declaringObject, findMethod(declaringObject.getClass(), methodName, arguments.convertToParameters()), valueClazz, arguments, forceAccess);
    }

    /**
     * Invokes a method on the specified object using the provided {@link Method} object and arguments.
     * This method wraps the provided arguments into a {@link MethodArguments} object and delegates the invocation to {@link #invokeMethod(Object, Method, Class, MethodArguments, boolean)}.
     * @param declaringObject The object on which to invoke the method
     * @param method The {@link Method} object representing the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return The result of the method invocation, cast to {@code valueClazz}
     * @throws ReflectionException If the method could not be invoked
     * @see #invokeMethod(Object, Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> T invokeMethod(E declaringObject, Method method, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeMethod(declaringObject, method, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Invokes the specified method on the given object with the provided {@link MethodArguments}.
     * The result is cast to the specified {@code valueClazz}, or {@link Void#INSTANCE} is returned if {@code valueClazz} is {@link Void}.
     * @param declaringObject The object on which to invoke the method
     * @param method The {@link Method} object representing the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return The result of the method invocation, cast to {@code valueClazz}
     * @throws ReflectionException If the method could not be invoked
     * @author NotKili
     */
    public static <T, E> T invokeMethod(E declaringObject, Method method, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        if (forceAccess) method.setAccessible(true);

        try {
            var result = method.invoke(declaringObject, arguments.getArguments());

            if (valueClazz != Void.class) {
                return valueClazz.cast(result);
            }

            return valueClazz.cast(Void.INSTANCE);
        } catch (Exception e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not invoke method '" + method.getName() + "' within class '" + declaringObject.getClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely invokes a method on the specified object using the provided method name and arguments.
     * Wraps the provided arguments into a {@link MethodArguments} object and delegates the invocation to the {@link #invokeMethodSafe(Object, String, Class, MethodArguments, boolean)} method.
     * @param declaringObject The object on which to invoke the method
     * @param methodName The name of the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the result of the method invocation cast to {@code valueClazz}, or {@link Uncertain#empty()} if the method could not be invoked
     * @see #invokeMethodSafe(Object, String, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> Uncertain<T> invokeMethodSafe(E declaringObject, String methodName, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeMethodSafe(declaringObject, methodName, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Safely invokes a method on the specified object using the provided method name and {@link MethodArguments}.
     * Finds the method using the {@link #findMethodSafe(Class, String, MethodParameters)} (Class, String, MethodParameters)} method using the given name and argument types, and then delegates the result to the {@link #invokeMethodSafe(Object, Method, Class, MethodArguments, boolean)} method.
     * @param declaringObject The object on which to invoke the method
     * @param methodName The name of the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the result of the method invocation cast to {@code valueClazz}, or {@link Uncertain#empty()} if the method could not be found or invoked
     * @see #invokeMethodSafe(Object, Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> Uncertain<T> invokeMethodSafe(E declaringObject, String methodName, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        return findMethodSafe(declaringObject.getClass(), methodName, arguments.convertToParameters()).ifPresentOrElseReturn(method -> invokeMethodSafe(declaringObject, method, valueClazz, arguments, forceAccess), Uncertain::empty);
    }

    /**
     * Safely invokes a method on the specified object using the provided {@link Method} object and arguments.
     * Wraps the provided arguments into a {@link MethodArguments} object and delegates the invocation to the {@link #invokeMethodSafe(Object, Method, Class, MethodArguments, boolean)} method.
     * @param declaringObject The object on which to invoke the method
     * @param method The {@link Method} object representing the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the result of the method invocation cast to {@code valueClazz}, or {@link Uncertain#empty()} if the method could not be invoked
     * @see #invokeMethodSafe(Object, Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> Uncertain<T> invokeMethodSafe(E declaringObject, Method method, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeMethodSafe(declaringObject, method, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Safely invokes the specified method on the given object with the provided {@link MethodArguments}.
     * The result is wrapped in an {@link Uncertain}, cast to the specified {@code valueClazz}, or {@link Uncertain#empty()} is returned if an exception occurs.
     * @param declaringObject The object on which to invoke the method
     * @param method The {@link Method} object representing the method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @param <E> The type of the declaring object
     * @return An {@link Uncertain} containing the result of the method invocation cast to {@code valueClazz}, or {@link Uncertain#empty()} if the method could not be invoked
     * @author NotKili
     */
    public static <T, E> Uncertain<T> invokeMethodSafe(E declaringObject, Method method, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        if (forceAccess) method.setAccessible(true);

        try {
            var result = method.invoke(declaringObject, arguments.getArguments());
            
            if (valueClazz != Void.class) {
                return Uncertain.of(valueClazz.cast(result));
            }
            
            return Uncertain.of(valueClazz.cast(Void.INSTANCE));
        } catch (Exception e) {
            return Uncertain.empty();
        }
    }

    /**
     * Invokes a static method on the specified class using the provided method name and arguments.
     * Wraps the provided arguments into a {@link MethodArguments} object and delegates the invocation to the {@link #invokeStaticMethod(Class, String, Class, MethodArguments, boolean)} method.
     * @param declaringClazz The class on which to invoke the static method
     * @param methodName The name of the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @param <E> The type of the declaring class
     * @return The result of the method invocation, cast to {@code valueClazz}
     * @throws ReflectionException If the static method could not be invoked
     * @see #invokeStaticMethod(Class, String, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> T invokeStaticMethod(Class<E> declaringClazz, String methodName, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeStaticMethod(declaringClazz, methodName, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Invokes a static method on the specified class using the provided method name and {@link MethodArguments}.
     * Finds the method using the {@link #findMethod(Class, String, MethodParameters)} method using the given name and argument types, and then delegates the result to the {@link #invokeStaticMethod(Method, Class, MethodArguments, boolean)} method.
     * @param declaringClazz The class on which to invoke the static method
     * @param methodName The name of the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @param <E> The type of the declaring class
     * @return The result of the method invocation, cast to {@code valueClazz}
     * @throws ReflectionException If the static method could not be found or invoked
     * @see #invokeStaticMethod(Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> T invokeStaticMethod(Class<E> declaringClazz, String methodName, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        return invokeStaticMethod(findStaticMethod(declaringClazz, methodName, arguments.convertToParameters()), valueClazz, arguments, forceAccess);
    }

    /**
     * Invokes a static method on the specified class using the provided {@link Method} object and arguments.
     * Wraps the provided arguments into a {@link MethodArguments} object and delegates the invocation to the {@link #invokeStaticMethod(Method, Class, MethodArguments, boolean)} arguments.
     * @param method The {@link Method} object representing the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @return The result of the method invocation, cast to {@code valueClazz}
     * @throws ReflectionException If the static method could not be invoked
     * @see #invokeStaticMethod(Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> T invokeStaticMethod(Method method, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeStaticMethod(method, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Invokes a static method on the specified class using the provided {@link Method} object and {@link MethodArguments}.
     * The result is cast to the specified {@code valueClazz}, or {@link  Void#INSTANCE} if the return type is {@link Void}.
     * @param method The {@link Method} object representing the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @return The result of the method invocation cast to {@code valueClazz}
     * @throws ReflectionException If the static method could not be invoked
     * @author NotKili
     */
    public static <T> T invokeStaticMethod(Method method, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        try {
            if (forceAccess) method.setAccessible(true);
            
            var result = method.invoke(null, arguments.getArguments());

            if (valueClazz != Void.class) {
                return valueClazz.cast(result);
            }

            return valueClazz.cast(Void.INSTANCE);
        } catch (Exception e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not invoke static method '" + method.getName() + "' within class '" + method.getDeclaringClass().getSimpleName() + "'", e);
        }
    }

    /**
     * Safely invokes a static method on the specified class using the provided method name and arguments.
     * Wraps the provided arguments into a {@link MethodArguments} object and delegates the invocation to the {@link #invokeStaticMethodSafe(Class, String, Class, MethodArguments, boolean)} method.
     * @param declaringClazz The class on which to invoke the static method
     * @param methodName The name of the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @param <E> The type of the declaring class
     * @return An {@link Uncertain} containing the result of the method invocation, cast to {@code valueClazz}, or empty if the method could not be invoked
     * @see #invokeStaticMethodSafe(Class, String, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> Uncertain<T> invokeStaticMethodSafe(Class<E> declaringClazz, String methodName, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeStaticMethodSafe(declaringClazz, methodName, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Safely invokes a static method on the specified class using the provided method name and {@link MethodArguments}.
     * Finds the method using the {@link #findStaticMethodSafe(Class, String, MethodParameters)} (Class, String, MethodParameters)} method using the given name and argument types, and then delegates the result to the {@link #invokeStaticMethodSafe(Method, Class, MethodArguments, boolean)} method.
     * @param declaringClazz The class on which to invoke the static method
     * @param methodName The name of the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @param <E> The type of the declaring class
     * @return An {@link Uncertain} containing the result of the method invocation, cast to {@code valueClazz}, or empty if the method could not be found or invoked
     * @see #invokeStaticMethodSafe(Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T, E> Uncertain<T> invokeStaticMethodSafe(Class<E> declaringClazz, String methodName, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        return findStaticMethodSafe(declaringClazz, methodName, arguments.convertToParameters()).ifPresentOrElseReturn(method -> invokeStaticMethodSafe(method, valueClazz, arguments, forceAccess), Uncertain::empty);
    }

    /**
     * Safely invokes a static method on the specified class using the provided {@link Method} object and arguments.
     * Wraps the provided arguments into a {@link MethodArguments} object and delegates the invocation to the {@link #invokeStaticMethodSafe(Method, Class, MethodArguments, boolean)} method.
     * @param method The {@link Method} object representing the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the method
     * @param <T> The return type of the method
     * @return An {@link Uncertain} containing the result of the method invocation, cast to {@code valueClazz}, or empty if the method could not be invoked
     * @see #invokeStaticMethodSafe(Method, Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> Uncertain<T> invokeStaticMethodSafe(Method method, Class<T> valueClazz, boolean forceAccess, Object... args) {
        return invokeStaticMethodSafe(method, valueClazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Safely invokes a static method on the specified class using the provided {@link Method} object and {@link MethodArguments}.
     * The result is cast to the specified {@code valueClazz}, or {@link Void#INSTANCE} is returned if the return type is {@link Void}.
     * @param method The {@link Method} object representing the static method to invoke
     * @param valueClazz The {@link Class} of the return value
     * @param arguments The {@link MethodArguments} object containing the arguments to pass to the method
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The return type of the method
     * @return An {@link Uncertain} containing the result of the method invocation cast to {@code valueClazz}, or empty if the method could not be invoked
     * @author NotKili
     */
    public static <T> Uncertain<T> invokeStaticMethodSafe(Method method, Class<T> valueClazz, MethodArguments arguments, boolean forceAccess) {
        try {
            if (forceAccess) method.setAccessible(true);
            
            var result = method.invoke(null, arguments.getArguments());

            if (valueClazz != Void.class) {
                return Uncertain.of(valueClazz.cast(result));
            }

            return Uncertain.of(valueClazz.cast(Void.INSTANCE));
        } catch (Exception e) {
            return Uncertain.empty();
        }
    }

    /**
     * Finds a method in the specified class or its superclasses based on the method name and parameter types.
     * Searches through the class hierarchy to locate the method.
     * @param declaringClazz The class in which to search for the method
     * @param method The name of the method to find
     * @param parameters The {@link MethodParameters} object containing the parameter types of the method
     * @param <T> The type of the declaring class
     * @return The {@link Method} object representing the method
     * @throws ReflectionException If the method could not be found
     * @author NotKili
     */
    public static <T> Method findMethod(Class<T> declaringClazz, String method, MethodParameters parameters) {
        Class<?> tmpClazz = declaringClazz;

        while (tmpClazz != null) {
            try {
                return tmpClazz.getDeclaredMethod(method, parameters.getParameters());
            } catch (Exception e) {
                tmpClazz = tmpClazz.getSuperclass();
            }
        }

        throw new ReflectionException(ERROR_PREFIX + "Could not locate method '" + method + "' within class '" + declaringClazz.getSimpleName() + "' or any of its Super-Classes");
    }

    /**
     * Safely attempts to find a method in the specified class or its superclasses based on the method name and parameter types.
     * Wraps the result in an {@link Uncertain} object, which is empty if the method could not be found.
     * @param declaringClazz The class in which to search for the method
     * @param method The name of the method to find
     * @param parameters The {@link MethodParameters} object containing the parameter types of the method
     * @param <T> The type of the declaring class
     * @return An {@link Uncertain} containing the {@link Method} object if found, or empty if the method could not be found
     * @see #findMethod(Class, String, MethodParameters) 
     * @author NotKili
     */
    public static <T> Uncertain<Method> findMethodSafe(Class<T> declaringClazz, String method, MethodParameters parameters) {
        try {
            return Uncertain.of(findMethod(declaringClazz, method, parameters));
        } catch (ReflectionException e) {
            return Uncertain.empty();
        }
    }

    /**
     * Finds a static method in the specified class or its superclasses based on the method name and parameter types.
     * Searches through the class hierarchy to locate the static method.
     * @param declaringClazz The class in which to search for the static method
     * @param method The name of the static method to find
     * @param parameters The {@link MethodParameters} object containing the parameter types of the method
     * @param <T> The type of the declaring class
     * @return The {@link Method} object representing the static method
     * @throws ReflectionException If the method could not be found
     * @author NotKili
     */
    public static <T> Method findStaticMethod(Class<T> declaringClazz, String method, MethodParameters parameters) {
        Class<?> tmpClazz = declaringClazz;

        while (tmpClazz != null) {
            try {
                return tmpClazz.getDeclaredMethod(method, parameters.getParameters());
            } catch (Exception e) {
                tmpClazz = tmpClazz.getSuperclass();
            }
        }

        throw new ReflectionException(ERROR_PREFIX + "Could not locate static method '" + method + "' within class '" + declaringClazz.getSimpleName() + "' or any of its Super-Classes");
    }

    /**
     * Safely attempts to find a static method in the specified class or its superclasses based on the method name and parameter types.
     * Wraps the result in an {@link Uncertain} object, which is empty if the method could not be found.
     * @param declaringClazz The class in which to search for the static method
     * @param method The name of the static method to find
     * @param parameters The {@link MethodParameters} object containing the parameter types of the method
     * @param <T> The type of the declaring class
     * @return An {@link Uncertain} containing the {@link Method} object if found, or empty if the method could not be found
     * @author NotKili
     */
    public static <T> Uncertain<Method> findStaticMethodSafe(Class<T> declaringClazz, String method, MethodParameters parameters) {
        try {
            return Uncertain.of(findStaticMethod(declaringClazz, method, parameters));
        } catch (ReflectionException e) {
            return Uncertain.empty();
        }
    }

    /**
     * Creates a new instance of the specified class using the provided arguments.
     * Wraps the provided list of arguments into a {@link MethodArguments} object and delegates the result to the {@link #newInstance(Class, MethodArguments, boolean)} method.
     * @param clazz The {@link Class} object representing the class to instantiate
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the constructor
     * @param <T> The type of the class
     * @return The newly created instance of type {@code T}
     * @throws ReflectionException If the instance could not be created
     * @see #newInstance(Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> T newInstance(Class<T> clazz, boolean forceAccess, Object... args) {
        return newInstance(clazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Creates a new instance of the specified class using the provided arguments.
     * Wraps the arguments in a {@link MethodArguments} object and delegates the result to the {@link #newInstance(Constructor, MethodArguments, boolean)} method.
     * @param clazz The {@link Class} object representing the class to instantiate
     * @param arguments The {@link MethodArguments} object containing the arguments for the constructor
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The type of the class
     * @return The newly created instance of type {@code T}
     * @throws ReflectionException If the instance could not be created
     * @see #newInstance(Constructor, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> T newInstance(Class<T> clazz, MethodArguments arguments, boolean forceAccess) {
        return newInstance(findConstructor(clazz, arguments.convertToParameters()), arguments, forceAccess);
    }

    /**
     * Creates a new instance of the class represented by the specified constructor using the provided arguments.
     * Wraps the provided list of arguments into a {@link MethodArguments} object and delegates the result to the {@link #newInstance(Constructor, MethodArguments, boolean)} method.
     * @param constructor The {@link Constructor} object representing the constructor to use
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the constructor
     * @param <T> The type of the class
     * @return The newly created instance of type {@code T}
     * @throws ReflectionException If the instance could not be created
     * @see #newInstance(Constructor, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> T newInstance(Constructor<T> constructor, boolean forceAccess, Object... args) {
        return newInstance(constructor, new MethodArguments(args), forceAccess);
    }

    /**
     * Creates a new instance of the class represented by the specified constructor using the provided arguments.
     * @param constructor The {@link Constructor} object representing the constructor to use
     * @param arguments The {@link MethodArguments} object containing the arguments for the constructor
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The type of the class
     * @return The newly created instance of type {@code T}
     * @throws ReflectionException If the instance could not be created
     * @author NotKili
     */
    public static <T> T newInstance(Constructor<T> constructor, MethodArguments arguments, boolean forceAccess) {
        try {
            if (forceAccess) constructor.setAccessible(true);
            
            return constructor.newInstance(arguments.getArguments());
        } catch (Exception e) {
            throw new ReflectionException(ERROR_PREFIX + "Could not invoke constructor '" + constructor.getName() + "(" + arguments.getClassesString() + ")'", e);
        }
    }

    /**
     * Creates a new instance of the specified class using the provided arguments in a safe manner.
     * Wraps the provided list of arguments into a {@link MethodArguments} object and delegates the result to the {@link #newInstanceSafe(Class, MethodArguments, boolean)} method.
     * @param clazz The {@link Class} object representing the class to instantiate
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the constructor
     * @param <T> The type of the class
     * @return An {@link Uncertain} containing the newly created instance of type {@code T} if successful, or empty if the instance could not be created
     * @see #newInstanceSafe(Class, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> Uncertain<T> newInstanceSafe(Class<T> clazz, boolean forceAccess, Object... args) {
        return newInstanceSafe(clazz, new MethodArguments(args), forceAccess);
    }

    /**
     * Creates a new instance of the specified class using the provided arguments in a safe manner.
     * Wraps the arguments in a {@link MethodArguments} object and delegates the result to the {@link #newInstanceSafe(Constructor, MethodArguments, boolean)} method.
     * @param clazz The {@link Class} object representing the class to instantiate
     * @param arguments The {@link MethodArguments} object containing the arguments for the constructor
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The type of the class
     * @return An {@link Uncertain} containing the newly created instance of type {@code T} if successful, or empty if the instance could not be created
     * @see #newInstanceSafe(Constructor, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> Uncertain<T> newInstanceSafe(Class<T> clazz, MethodArguments arguments, boolean forceAccess) {
        return findConstructorSafe(clazz, arguments.convertToParameters()).ifPresentOrElseReturn(constructor -> newInstanceSafe(constructor, arguments, forceAccess), Uncertain::empty);
    }

    /**
     * Creates a new instance of the class represented by the specified constructor using the provided arguments in a safe manner.
     * Wraps the provided list of arguments into a {@link MethodArguments} object and delegates the result to the {@link #newInstanceSafe(Constructor, MethodArguments, boolean)} method.
     * @param constructor The {@link Constructor} object representing the constructor to use
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param args The arguments to pass to the constructor
     * @param <T> The type of the class
     * @return An {@link Uncertain} containing the newly created instance of type {@code T} if successful, or empty if the instance could not be created
     * @see #newInstanceSafe(Constructor, MethodArguments, boolean)
     * @author NotKili
     */
    public static <T> Uncertain<T> newInstanceSafe(Constructor<T> constructor, boolean forceAccess, Object... args) {
        return newInstanceSafe(constructor, new MethodArguments(args), forceAccess);
    }

    /**
     * Creates a new instance of the class represented by the specified constructor using the provided arguments in a safe manner.
     * @param constructor The {@link Constructor} object representing the constructor to use
     * @param arguments The {@link MethodArguments} object containing the arguments for the constructor
     * @param forceAccess {@code true} if access should be forcibly granted, {@code false} otherwise
     * @param <T> The type of the class
     * @return An {@link Uncertain} containing the newly created instance of type {@code T} if successful, or empty if the instance could not be created
     * @author NotKili
     */
    public static <T> Uncertain<T> newInstanceSafe(Constructor<T> constructor, MethodArguments arguments, boolean forceAccess) {
        try {
            if (forceAccess) constructor.setAccessible(true);
            
            return Uncertain.of(constructor.newInstance(arguments.getArguments()));
        } catch (Exception e) {
            return Uncertain.empty();
        }
    }

    /**
     * Finds the constructor of the specified class that matches the provided parameters.
     * @param clazz The {@link Class} object representing the class whose constructor is to be found
     * @param parameters The {@link MethodParameters} object containing the parameter types for the constructor
     * @param <T> The type of the class
     * @return The {@link Constructor} object representing the constructor that matches the specified parameters
     * @throws ReflectionException If the constructor could not be located
     * @author NotKili
     */
    public static <T> Constructor<T> findConstructor(Class<T> clazz, MethodParameters parameters) {
        try {
            return clazz.getConstructor(parameters.getParameters());
        } catch (Exception ignored) {}

        throw new ReflectionException(ERROR_PREFIX + "Could not locate constructor '" + clazz.getSimpleName() + "(" + parameters.getClassesString() + ")' within class '" + clazz.getSimpleName() + "'");
    }

    /**
     * Safely finds the constructor of the specified class that matches the provided parameters.
     * Returns an {@link Uncertain} containing the {@link Constructor} object if found, or empty if not.
     * @param clazz The {@link Class} object representing the class whose constructor is to be found
     * @param parameters The {@link MethodParameters} object containing the parameter types for the constructor
     * @param <T> The type of the class
     * @return An {@link Uncertain} containing the {@link Constructor} object if found, or empty if not
     * @author NotKili
     */
    public static <T> Uncertain<Constructor<T>> findConstructorSafe(Class<T> clazz, MethodParameters parameters) {
        try {
            return Uncertain.of(findConstructor(clazz, parameters));
        } catch (ReflectionException e) {
            return Uncertain.empty();
        }
    }
}
