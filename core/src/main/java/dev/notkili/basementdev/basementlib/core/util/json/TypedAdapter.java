package dev.notkili.basementdev.basementlib.core.util.json;

import com.google.gson.*;
import dev.notkili.basementdev.basementlib.core.util.misc.cache.Cache;
import dev.notkili.basementdev.basementlib.core.util.registry.impl.key.SimpleStringKey;
import dev.notkili.basementdev.basementlib.core.util.typed.Type;
import dev.notkili.basementdev.basementlib.core.util.typed.TypeRegistry;
import dev.notkili.basementdev.basementlib.core.util.typed.Typed;

/**
 * A {@link JsonAdapter} for {@link Typed} objects using a {@link TypeRegistry} for automatic type identification.
 * @param <T> The type of object that this adapter can serialize and deserialize.
 * @author NotKili 
 * @since 1.0.0
 */
public class TypedAdapter<T extends Typed<?>> implements JsonAdapter<T> {
    /**
     * A cache of {@link TypedAdapter} instances for each {@link Typed} class to prevent duplicate instances serving the same purpose.
     * @since 1.0.0
     */
    public static final Cache<Class<? extends Typed<?>>, TypedAdapter<? extends Typed<?>>> CACHED_ADAPTERS = Cache.concurrent("TypedAdapter");

    /**
     * Creates a new {@link TypedAdapter} for the given {@link TypeRegistry} using the underlying {@link Cache} creation policy. <br>
     * If an adapter for the given registry already exists, it will be returned instead.
     * @param registry The {@link TypeRegistry} to create the adapter for.
     * @return A new or existing {@link TypedAdapter} for the given registry.
     * @param <T> The type of object that this adapter can serialize and deserialize.
     * @author NotKili
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T extends Typed<?>> TypedAdapter<T> of(TypeRegistry<T> registry) {
        return (TypedAdapter<T>) CACHED_ADAPTERS.get(registry.baseClazz(), () -> new TypedAdapter<>(registry));
    }

    private final TypeRegistry<T> registry;

    private TypedAdapter(TypeRegistry<T> registry) {
        this.registry = registry;
    }

    /**
     * 
     * @param jsonElement The Json data being deserialized
     * @param type The type of the Object to deserialize to
     * @param jsonDeserializationContext The context of the deserialization
     * @return The deserialized object
     * @throws JsonParseException If the type identifier is missing, not a string, the type is unknown or if any other exception is thrown by the context.
     * @author NotKili
     * @since 1.0.0
     */
    @Override
    public T deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var jsonObject = jsonElement.getAsJsonObject();

        if (!jsonObject.has(Type.JSON_IDENTIFIER))
            throw new JsonParseException("[TypedAdapter] | " + registry.getName() + ": Missing type identifier in json object: \n" + jsonObject);

        var typeElem = jsonObject.get(Type.JSON_IDENTIFIER);
        
        if (!(typeElem instanceof JsonPrimitive))
            throw new JsonParseException("[TypedAdapter] | " + registry.getName() + ": Type identifier must be a string: \n" + jsonObject);
        
        var key = SimpleStringKey.of(typeElem.getAsString());
        var entry = registry.valueSafe(key);

        if (entry.isMissing())
            throw new JsonParseException("[TypeAdapter] | " + registry.getName() + ": Unknown type '" + key.key() + "'. Available types: " + registry.printKeys());
        
        return jsonDeserializationContext.deserialize(jsonObject, entry.get().clazz());
    }

    /**
     * Serializes the object to Json and adds the type identifier to the Json object. <br>
     * If the object already contains a type identifier, a {@link JsonParseException} will be thrown.
     * @param t the object that needs to be converted to Json.
     * @param type the actual type (fully genericized version) of the source object.
     * @param jsonSerializationContext the context of the serialization.
     * @return a JsonElement with the type identifier added.
     * @throws JsonParseException If the object already contains a type identifier or if any other exception is thrown by the context.
     * @author NotKili
     * @since 1.0.0
     */
    @Override
    public JsonElement serialize(T t, java.lang.reflect.Type type, JsonSerializationContext jsonSerializationContext) throws JsonParseException {
        var jsonObject = jsonSerializationContext.serialize(t).getAsJsonObject();
        
        if (jsonObject.has(Type.JSON_IDENTIFIER))
            throw new JsonParseException("[TypedAdapter] | " + registry.getName() + ": Json object already contains a type identifier: \n" + jsonObject);
        
        jsonObject.addProperty(Type.JSON_IDENTIFIER, t.type().name());
        
        return jsonObject;
    }
}
