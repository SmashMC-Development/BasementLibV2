package dev.notkili.basementdev.basementlib.core.util.exception;

import dev.notkili.basementdev.basementlib.core.util.misc.INamed;
import lombok.Getter;

@Getter
public class NamedException extends RuntimeException {
    private final INamed named;

    public NamedException(INamed named, String message) {
        super(message);

        this.named = named;
    }

    public NamedException(INamed named, Throwable throwable) {
        super(throwable);

        this.named = named;
    }

    public NamedException(INamed named, String message, Throwable throwable) {
        super(message, throwable);

        this.named = named;
    }

    @Override
    public String getMessage() {
        return "[" + this.prefix() + this.named.name() + "] threw an exception: " + super.getMessage();
    }

    public String getOriginalMessage() {
        return super.getMessage();
    }

    public String prefix() {
        return "";
    }
}
