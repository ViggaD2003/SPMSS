package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.jobs.enums.QuartzJobDefinition;
import org.quartz.Scheduler;

import java.util.List;
import java.util.Map;

public interface QuartzSchedulerService {

    void scheduleUpdateSlotStatus(String cronExpression);
    void scheduleUpdateAppointmentStatus(String cronExpression);
    void scheduleUpdateSurveyStatus(String cronExpression);
    void scheduleUpdateClassStatus(String cronExpression);
    void scheduleProcessRecurringSurveyStatus(String cronExpression);
    void scheduleJobs(QuartzJobDefinition definition, String cronExpression);

    List<Map<String, Object>> getAllScheduledJobs();

    Scheduler getScheduler();
}
