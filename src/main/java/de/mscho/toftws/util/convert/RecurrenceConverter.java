package de.mscho.toftws.util.convert;

import de.mscho.toftws.calendar.entity.Recurrence;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecurrenceConverter implements Converter<String, Recurrence> {

    @Override
    public Recurrence convert(String source) {
        String uppercase = source.toUpperCase();
        return Recurrence.valueOf(uppercase);
    }
}
