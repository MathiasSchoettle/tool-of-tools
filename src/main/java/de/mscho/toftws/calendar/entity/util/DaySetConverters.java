package de.mscho.toftws.calendar.entity.util;

import com.fasterxml.jackson.databind.util.StdConverter;
import jakarta.persistence.AttributeConverter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Converts Set of WeekDays to a single Comma seperated String and vice versa.
 */
public class DaySetConverters {

    /**
     * Will filter out invalid values
     */
    public static class DaySetConverter extends StdConverter<String, SortedSet<DayOfWeek>> {
        @Override
        public SortedSet<DayOfWeek> convert(String s) {
            List<String> dayNames = Arrays.stream(s.split(",")).toList();
            return Arrays.stream(DayOfWeek.values())
                    .filter(dayOfWeek -> dayNames.contains(dayOfWeek.name()))
                    .collect(Collectors.toCollection(TreeSet::new));
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
