package dev.notkili.basementdev.basementlib.core.util.reflection;

public class ReflectionException extends RuntimeException {
    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(Throwable cause) {
    super(cause);
  }
  
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
