package dev.notkili.basementdev.basementlib.core.util.misc;

/**
 * Marks a class to be hash-able <br>
 * Due to restrictions within java, this interface does not override the default {@link Object#hashCode()} method. <br>
 * Any implementor of this interface should override the {@link Object#hashCode()} method and call {@link #hash()} if needed.
 * @see Object#hashCode()
 * @author NotKili
 * @since 1.0
 */
public interface Hashable {
    /**
     * Generates a hash code for this object. <br>
     * A hash code is a (usually) unique identifier for an object, used in hash maps and sets.
     * @return The hash code.
     * @see Object#hashCode()
     * @author NotKili
     * @since 1.0
     */
    int hash();
}
