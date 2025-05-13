package com.grepp.matnam.app.controller.api.admin.annotation;

import com.grepp.matnam.app.controller.api.admin.validator.AtLeastOneMoodSelectedValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AtLeastOneMoodSelectedValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneMoodSelected {

    String message() default "최소 1개 이상의 분위기를 선택해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
