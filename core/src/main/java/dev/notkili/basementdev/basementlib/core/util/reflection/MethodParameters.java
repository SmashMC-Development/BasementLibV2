package dev.notkili.basementdev.basementlib.core.util.reflection;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public class MethodParameters {
    private final Class<?>[] parameters;
    
    public MethodParameters(Class<?>... parameters) {
        this.parameters = parameters;
    }
    
    public String getClassesString() {
        var result = new StringBuilder();
        
        for (int i = 0; i < parameters.length; i++) {
            result.append(parameters[i].getSimpleName());
            if (i != parameters.length - 1) {
                result.append(", ");
            }
        }
        
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodParameters methodParameters = (MethodParameters) o;
        return Objects.deepEquals(parameters, methodParameters.parameters);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(parameters);
    }
}
