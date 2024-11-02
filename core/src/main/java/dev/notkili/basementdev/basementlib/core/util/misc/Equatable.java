package dev.notkili.basementdev.basementlib.core.util.misc;

/**
 * Marks a class to be equatable, therefore "comparable" to other objects. <br>
 * Due to restrictions within java, this interface does not override the default {@link Object#equals(Object)} method. <br>
 * Any implementor of this interface should override the {@link Object#equals(Object)} method and call {@link #equal(Object)} if needed.
 * @see Object#equals(Object)
 * @author NotKili 
 * @since 1.0
 */
public interface Equatable {
    /**
     * Compares this object to another object.
     * @param obj The object to compare to.
     * @return Whether the objects are equal.
     * @see Object#equals(Object)
     * @author NotKili
     * @since 1.0
     */
    boolean equal(Object obj);
}
