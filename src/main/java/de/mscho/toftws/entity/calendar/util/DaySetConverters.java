package de.mscho.toftws.entity.calendar.util;

import com.fasterxml.jackson.databind.util.StdConverter;

import javax.persistence.AttributeConverter;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converts Set of WeekDays to a single Comma seperated String and vice versa.
 */
public class DaySetConverters {

    public static class DaySetConverter extends StdConverter<String, Set<DayOfWeek>> {
        @Override
        public Set<DayOfWeek> convert(String s) {
            return Arrays.stream(s.split(",")).map(DayOfWeek::valueOf).collect(Collectors.toSet());
        }
    }

    public static class DaySetAttributeConverter implements AttributeConverter<Set<DayOfWeek>, String> {
        @Override
        public String convertToDatabaseColumn(Set<DayOfWeek> days) {
            return days.stream().map(Enum::toString).collect(Collectors.joining(","));
        }
        @Override
        public Set<DayOfWeek> convertToEntityAttribute(String s) {
            return Arrays.stream(s.split(",")).map(DayOfWeek::valueOf).collect(Collectors.toSet());
        }
    }
}
