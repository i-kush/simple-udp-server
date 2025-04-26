package com.kush.udp;

import java.util.Collection;
import java.util.Collections;

//That's something I lost during the migration to GitHub, so I need to remember what we should do here :)
public class CefMessage {

    private final String message;

    public CefMessage(String message) {
        this.message = message;
    }

    public Collection<Object> getIncorrect() {
        return Collections.emptyList();
    }

    public Collection<Object> getMissing() {
        return Collections.emptyList();
    }

    public Collection<Object> getOtherErrors() {
        return Collections.emptyList();
    }

    public String getMessage() {
        return message;
    }
}
