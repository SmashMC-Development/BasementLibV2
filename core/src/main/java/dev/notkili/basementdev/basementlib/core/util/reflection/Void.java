package dev.notkili.basementdev.basementlib.core.util.reflection;

/**
 * A helper class used in the reflection library to represent a "Void" return type for methods that are reflected to have a "Void" return type.
 * The class is instantiated once and used throughout the library with the static {@link Void#INSTANCE}.
 * The actual return value of a reflected method will be "null", this helper class is just used define the return type.
 * @author NotKili
 */
public class Void {
    public static Void INSTANCE = new Void();
    
    private Void() {
    }

    @Override
    public String toString() {
        return "<Void>";
    }
}
