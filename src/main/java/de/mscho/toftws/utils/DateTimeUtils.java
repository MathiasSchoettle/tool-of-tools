package de.mscho.toftws.utils;

import java.time.*;

public class DateTimeUtils {
    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final Instant MAX_INSTANT_TIME_SQL = LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.MIN).toInstant(ZoneOffset.UTC);

    private static final long SECONDS_IN_UTC_DAY = 86_400L;

    public static boolean isInstantAtMidnight(Instant instant) {
        return LocalTime.from(instant.atZone(UTC)).equals(LocalTime.MIDNIGHT);
    }

    public static boolean areUTCDay(long second) {
        return second % SECONDS_IN_UTC_DAY == 0L;
    }
}
