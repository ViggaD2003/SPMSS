package com.fpt.gsu25se47.schoolpsychology.validations;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSupportProgramRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDateConstraint, Object>{

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) return true;

        return switch (object) {
            case CreateSupportProgramRequest request -> {
                if (request.getStartDate() == null || request.getEndDate() == null) {
                    yield true;
                }
                yield request.getEndDate().isAfter(request.getStartDate());
            }
            default -> throw new IllegalStateException("Unexpected value: " + object);
        };
    }
}
