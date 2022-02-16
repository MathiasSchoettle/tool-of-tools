package de.mscho.toftws.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class TimeDateProperties {

    public static final OffsetDateTime MAX_OFFSET_DATETIME = OffsetDateTime.of(LocalDateTime.of(9999, 12, 31, 23, 59, 59), ZoneOffset.UTC);

    private TimeDateProperties() {
        //
    }
}
