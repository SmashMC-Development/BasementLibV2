package dev.notkili.basementdev.basementlib.core.util.registry;

/**
 * An exception that is thrown when a registry encounters any error. <br>
 * The exception message will contain the registry name and the message that was passed to the constructor.
 * @author NotKili
 * @since 1.0.0
 */
public class RegistryException extends RuntimeException {
    public RegistryException(IRegistry<?, ?> registry, String message) {
        super("[Registry: " + registry.getName() + "] threw an exception: " + message);
    }
    
    public RegistryException(IRegistry<?, ?> registry, Throwable throwable) {
        super("[Registry: " + registry.getName() + "] threw an exception:", throwable);
    }
    
    public RegistryException(IRegistry<?, ?> registry, String message, Throwable throwable) {
        super("[Registry: " + registry.getName() + "] threw an exception: " + message, throwable);
    }
}
