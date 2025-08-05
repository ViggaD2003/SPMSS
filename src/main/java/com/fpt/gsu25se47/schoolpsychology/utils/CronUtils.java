package com.fpt.gsu25se47.schoolpsychology.utils;

import com.cronutils.builder.CronBuilder;
import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.parser.CronParser;

import static com.cronutils.model.field.expression.FieldExpression.always;
import static com.cronutils.model.field.expression.FieldExpression.questionMark;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;


public class CronUtils {

    public static String buildCronExpressionFromTime(Integer hour, Integer minute, Integer second) {

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withSecond(onOrEvery(second))
                .withMinute(onOrEvery(minute))
                .withHour(onOrEvery(hour))
                .withDoM(questionMark())
                .withMonth(always())
                .withDoW(always())
                .withYear(always())
                .instance();

        return cron.asString();
    }

    public static String describeCronExpression(String cronExpression) {

        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

        CronDescriptor descriptor = CronDescriptor.instance();

        return descriptor.describe(parser.parse(cronExpression));
    }

    private static FieldExpression onOrEvery(Integer value) {
        if (value == null) {
            return always();
        } else {
            return on(value);
        }
    }
}
