package de.mscho.toftws.exception;

import java.util.Arrays;

public class NameFormatException extends Exception {

    public NameFormatException(String ... names) {
        super("One or more names have unsupported Format: " + Arrays.toString(names));
    }
}
