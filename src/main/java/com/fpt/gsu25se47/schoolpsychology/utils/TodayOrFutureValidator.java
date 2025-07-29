package com.fpt.gsu25se47.schoolpsychology.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class TodayOrFutureValidator implements ConstraintValidator<TodayOrFuture, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return true; // let @NotNull handle null check
        return !value.isBefore(LocalDateTime.now()); // now or future
    }
}
