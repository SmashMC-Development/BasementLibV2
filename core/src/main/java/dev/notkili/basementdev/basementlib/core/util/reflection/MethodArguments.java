package dev.notkili.basementdev.basementlib.core.util.reflection;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public class MethodArguments {
    private final Object[] arguments;
    
    public MethodArguments(Object... args) {
        this.arguments = args;
    }
    
    public MethodParameters convertToParameters() {
        return new MethodParameters(getClasses());
    }

    public Class<?>[] getClasses() {
        var result = new Class<?>[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            result[i] = arguments[i].getClass();
        }

        return result;
    }
    
    public String getClassesString() {
        var result = new StringBuilder();
        
        for (int i = 0; i < arguments.length; i++) {
            result.append(arguments[i].getClass().getSimpleName());
            if (i != arguments.length - 1) {
                result.append(", ");
            }
        }
        
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodArguments methodArguments = (MethodArguments) o;
        return Objects.deepEquals(getClasses(), methodArguments.getClasses());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getClasses());
    }
}
