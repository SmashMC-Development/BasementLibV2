package dev.notkili.basementdev.basementlib.core.util.misc.manager;

import dev.notkili.basementdev.basementlib.core.util.exception.NamedException;
import dev.notkili.basementdev.basementlib.core.util.misc.INamed;

public class ManagerException extends NamedException {

    public ManagerException(INamed named, String message) {
        super(named, message);
    }

    public ManagerException(INamed named, Throwable throwable) {
        super(named, throwable);
    }

    public ManagerException(INamed named, String message, Throwable throwable) {
        super(named, message, throwable);
    }

    @Override
    public String prefix() {
        return "Manager: ";
    }
}
