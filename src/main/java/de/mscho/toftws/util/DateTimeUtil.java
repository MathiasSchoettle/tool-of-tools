package de.mscho.toftws.util;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    public static final DateTimeFormatter DTF_YEAR_MONTH_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeUtil() {
        //
    }
}
