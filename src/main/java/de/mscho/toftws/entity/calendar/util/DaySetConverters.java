package de.mscho.toftws.entity.calendar.util;

import com.fasterxml.jackson.databind.util.StdConverter;

import jakarta.persistence.AttributeConverter;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.stream.Collectors;

/**
 * Converts Set of WeekDays to a single Comma seperated String and vice versa.
 */
public class DaySetConverters {

    public static class DaySetConverter extends StdConverter<String, SortedSet<DayOfWeek>> {
        @Override
        public SortedSet<DayOfWeek> convert(String s) {
            return Arrays.stream(s.split(",")).map(DayOfWeek::valueOf).collect(Collectors.toCollection(TreeSet::new));
        }
    }

    public static class DaySetAttributeConverter implements AttributeConverter<SortedSet<DayOfWeek>, String> {
        @Override
        public String convertToDatabaseColumn(SortedSet<DayOfWeek> days) {
            return days.stream().map(Enum::toString).collect(Collectors.joining(","));
        }
        @Override
        public SortedSet<DayOfWeek> convertToEntityAttribute(String s) {
            return Arrays.stream(s.split(",")).map(DayOfWeek::valueOf).collect(Collectors.toCollection(TreeSet::new));
        }
    }
}
