package com.fpt.gsu25se47.schoolpsychology.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TodayOrFutureValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TodayOrFuture {
    String message() default "Thời gian phải là hôm nay hoặc tương lai";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
