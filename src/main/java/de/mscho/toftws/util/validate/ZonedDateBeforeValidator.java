package de.mscho.toftws.util.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZonedDateTime;

public class ZonedDateBeforeValidator implements ConstraintValidator<ZonedDateBefore, Object> {
    private String startFieldName;
    private String endFieldName;
    private boolean inclusive;
    private ZonedDateTime start;
    private ZonedDateTime end;

    @Override
    public void initialize(ZonedDateBefore constraintAnnotation) {
        startFieldName = constraintAnnotation.startField();
        endFieldName = constraintAnnotation.endField();
        inclusive = constraintAnnotation.inclusive();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            fillDates(o);
            return checkDates();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if start is before end.
     * If the check is inclusive, start may be the same as end.
     */
    private boolean checkDates() {
        if (inclusive) {
            return !end.isBefore(start);
        }

        return start.isBefore(end);
    }

    /**
     * Get date values from parent object.
     */
    private void fillDates(Object o) throws NoSuchFieldException, IllegalAccessException {
        var c = o.getClass();
        var startField = c.getField(startFieldName);
        var endField = c.getField(endFieldName);
        start = (ZonedDateTime) startField.get(o);
        end = (ZonedDateTime) endField.get(o);
    }
}
