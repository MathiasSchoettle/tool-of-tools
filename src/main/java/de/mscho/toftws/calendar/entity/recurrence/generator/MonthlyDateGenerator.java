package de.mscho.toftws.calendar.entity.recurrence.generator;

import de.mscho.toftws.util.DateTimeProperties;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class MonthlyDateGenerator extends RecurrenceDateGenerator {

    private final int monthCount;

    private static final int MAX_MONTH_COUNT = 12;

    public MonthlyDateGenerator(int monthCount) {

        if (monthCount < 1 || monthCount > MAX_MONTH_COUNT)
            throw new IllegalArgumentException("Month count must be larger than 0 and lesser or equal than " + MAX_MONTH_COUNT);

        this.monthCount = monthCount;
    }

    @Override
    protected OffsetDateTime getFirstOccurrenceWithStartBeforeFrom(OffsetDateTime from, OffsetDateTime to, OffsetDateTime start) {

        long monthsBetween = ChronoUnit.MONTHS.between(start, from);
        monthsBetween += isWithSameDayAndTime(start, from) ? 0 : 1;

        long moduloOffset = monthsBetween % monthCount;
        monthsBetween += moduloOffset == 0 ? 0 : monthCount - moduloOffset;

        OffsetDateTime firstDateTime = start.plusMonths(monthsBetween);

        while (!isWithSameDayAndTime(firstDateTime, start)) {
            monthsBetween += monthCount;
            firstDateTime = start.plusMonths(monthsBetween);
        }

        if (firstDateTime.isAfter(to))
            return DateTimeProperties.MAX_OFFSET_DATETIME;

        return firstDateTime;
    }

    @Override
    public OffsetDateTime getNextOccurrence(OffsetDateTime dateTime) {

        int monthsToAdd = monthCount;
        OffsetDateTime nextDateTime = dateTime.plusMonths(monthsToAdd);

        while (!isWithSameDayAndTime(dateTime, nextDateTime)) {
            monthsToAdd += monthCount;
            nextDateTime = dateTime.plusMonths(monthsToAdd);
        }

        return nextDateTime;
    }

    @Override
    public OffsetDateTime getLastOccurrence(OffsetDateTime start, OffsetDateTime end) {
        return null;
    }

    private boolean isWithSameDayAndTime(OffsetDateTime first, OffsetDateTime second) {

        boolean isSameDay = first.getDayOfMonth() == second.getDayOfMonth();
        boolean isSameTime = first.toLocalTime().equals(second.toLocalTime());
        boolean isSameZone = first.getOffset().equals(second.getOffset());

        return isSameDay && isSameTime && isSameZone;
    }
}
