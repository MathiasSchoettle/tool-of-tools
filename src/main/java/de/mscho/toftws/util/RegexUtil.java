package de.mscho.toftws.util;

import java.util.Arrays;
import java.util.Objects;

public class RegexUtil {

    private static final String nameRegex = "^[a-zA-ZäÄöÖüÜß-]*$";

    private RegexUtil() {
        //
    }

    // TODO does not really cover everything but should be sufficient for most german names
    public static boolean areValidNames(String... names) {
        return Arrays.stream(names).allMatch(n -> n != null && n.matches(nameRegex));
    }
}
