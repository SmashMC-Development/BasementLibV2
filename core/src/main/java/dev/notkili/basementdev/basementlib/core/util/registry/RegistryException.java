package dev.notkili.basementdev.basementlib.core.util.registry;

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
