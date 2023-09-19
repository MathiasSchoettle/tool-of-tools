package de.mscho.toftws.calendar.entity.util;

import de.mscho.toftws.calendar.entity.payload.CalendarEventRequest;
import de.mscho.toftws.calendar.service.CategoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class EventRequestValidator implements ConstraintValidator<EventRequestConstraint, CalendarEventRequest> {

    private final CategoryService categoryService;
    private final String NOT_NECESSARY = "not necessary";
    private final String REQUIRED = "required";

    @Override
    public void initialize(EventRequestConstraint constraintAnnotation) {
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
        if (CollectionUtils.isEmpty(request.weekDays)) errors.put("weekDays", REQUIRED);

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
}
