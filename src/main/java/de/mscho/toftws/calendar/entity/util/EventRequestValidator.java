package de.mscho.toftws.calendar.entity.util;

import de.mscho.toftws.calendar.entity.payload.CalendarEventRequest;
import de.mscho.toftws.calendar.service.CategoryService;
import de.mscho.toftws.utils.DateTimeUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import static de.mscho.toftws.utils.DateTimeUtils.areUTCDay;
import static de.mscho.toftws.utils.DateTimeUtils.isInstantAtMidnight;

@RequiredArgsConstructor
@Component
public class EventRequestValidator implements ConstraintValidator<EventRequestValidator.Constraint, CalendarEventRequest> {

    private final CategoryService categoryService;
    private final String NOT_NECESSARY = "not necessary";
    private final String REQUIRED = "required";

    @Override
    public void initialize(EventRequestValidator.Constraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validation of Request fields depends on the recurrence type
     */
    @Override
    public boolean isValid(CalendarEventRequest request, ConstraintValidatorContext context) {
        var errors = switch (request.type) {
            case SINGLE -> validateSingle(request);
            case DAILY, MONTHLY, YEARLY -> validateDefaultRecurrence(request);
            case WEEKLY -> validateWeekly(request);
        };

        if (!categoryService.categoryExists(request.categoryId)) {
            String NOT_FOUND = "not found";
            errors.put("categoryId", NOT_FOUND);
        }

        if (request.fullDay) {
            if (request.end != null && !isInstantAtMidnight(request.end)) errors.put("end", "must be at start of day");
            if (!isInstantAtMidnight(request.start)) errors.put("start", "must be at start of day");
            if (areUTCDay(request.duration)) errors.put("duration", "must be a multiple of seconds in a day for full day events");
            if (!request.zoneId.equals(DateTimeUtils.UTC)) errors.put("zoneId", "must be UTC for full day events");
        }

        if (errors.isEmpty()) return true;

        addErrors(errors, context);
        context.disableDefaultConstraintViolation();
        return false;
    }

    private void addErrors(Map<String, String> errors, ConstraintValidatorContext context) {
        for (var error : errors.entrySet()) {
            context.buildConstraintViolationWithTemplate(error.getValue())
                    .addPropertyNode(error.getKey())
                    .addConstraintViolation();
        }
    }

    private Map<String, String> validateSingle(CalendarEventRequest request) {
        var errors = new HashMap<String, String>();

        if (request.end != null) errors.put("end", NOT_NECESSARY);
        if (request.offset != null) errors.put("offset", NOT_NECESSARY);
        if (request.weekDays != null) errors.put("weekDays", NOT_NECESSARY);
        if (request.occurrences != null) errors.put("occurrences", NOT_NECESSARY);

        return errors;
    }

    private Map<String, String> validateDefaultRecurrence(CalendarEventRequest request) {
        var errors = validateDatesOrOccurrence(request);

        if (request.offset == null) errors.put("offset", REQUIRED);
        if (request.weekDays != null) errors.put("weekDays", NOT_NECESSARY);

        return errors;
    }

    private Map<String, String> validateWeekly(CalendarEventRequest request) {
        var errors = validateDatesOrOccurrence(request);

        if (request.offset == null) errors.put("offset", REQUIRED);

        if (CollectionUtils.isEmpty(request.weekDays)) {
            errors.put("weekDays", REQUIRED);
        } else if (!request.weekDays.contains(request.start.atZone(request.zoneId).getDayOfWeek())) {
            errors.put("weekDays", "does not contain weekday of start date");
        }

        return errors;
    }

    private Map<String, String> validateDatesOrOccurrence(CalendarEventRequest request) {
        var errors = new HashMap<String, String>();

        if (request.end != null && request.occurrences != null)
            errors.put("occurrences", "not necessary when end is set");

        if (request.end == null && request.occurrences == null)
            errors.put("end", "necessary when occurrences not set");

        if (request.end != null && request.end.isBefore(request.start.plusSeconds(request.duration)))
            errors.put("end", "must be after first occurrence");

        return errors;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @jakarta.validation.Constraint(validatedBy = EventRequestValidator.class)
    public @interface Constraint {
        String message() default "Event request not valid";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
