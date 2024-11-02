package dev.notkili.basementdev.basementlib.core.util.misc;

/**
 * Marks a class to be printable, therefore able to be converted into a string representation. <br>
 * Due to restrictions within java, this interface does not override the default {@link Object#toString()} method. <br>
 * Any implementor of this interface should override the {@link Object#toString()} method and call {@link #print()} if needed.
 * @see Object#toString()
 * @author NotKili
 * @since 1.0
 */
public interface Printable {
    /**
     * Converts this object into a string representation.
     * @return The string representation of this object.
     * @see Object#toString()
     * @author NotKili
     * @since 1.0
     */
    String print();
}
