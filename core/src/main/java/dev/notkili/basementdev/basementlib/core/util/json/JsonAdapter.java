package dev.notkili.basementdev.basementlib.core.util.json;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * A simple interface that combines {@link JsonSerializer} and {@link JsonDeserializer} into one.
 * @param <T> The type of object that this adapter can serialize and deserialize.
 * @see JsonSerializer
 * @see JsonDeserializer
 * @author NotKili
 * @since 1.0.0
 */
public interface JsonAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {}
