package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.jobs.*;
import com.fpt.gsu25se47.schoolpsychology.jobs.enums.QuartzJobDefinition;
import com.fpt.gsu25se47.schoolpsychology.service.inter.QuartzSchedulerService;
import com.fpt.gsu25se47.schoolpsychology.utils.CronUtils;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuartzSchedulerServiceImpl implements QuartzSchedulerService {

    private final Scheduler scheduler;

    @Override
    public void scheduleUpdateSlotStatus(String cronExpression) {

        QuartzJobDefinition definition = QuartzJobDefinition.SLOT_STATUS;

        JobDetail jobDetail = JobBuilder.newJob(UpdateSlotsStatusJob.class)
                .withIdentity(definition.getJobName(), definition.getGroupName())
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(definition.getTriggerName(), definition.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();

        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scheduleUpdateAppointmentStatus(String cronExpression) {

        QuartzJobDefinition definition = QuartzJobDefinition.APPOINTMENT_STATUS;

        JobDetail jobDetail = JobBuilder.newJob(UpdateAppointmentsStatusJob.class)
                .withIdentity(definition.getJobName(), definition.getGroupName())
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(definition.getTriggerName(), definition.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();

        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scheduleUpdateSurveyStatus(String cronExpression) {

        QuartzJobDefinition definition = QuartzJobDefinition.SURVEY_STATUS;

        JobDetail jobDetail = JobBuilder.newJob(UpdateSurveysStatusJob.class)
                .withIdentity(definition.getJobName(), definition.getGroupName())
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(definition.getTriggerName(), definition.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();

        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scheduleUpdateClassStatus(String cronExpression) {

        QuartzJobDefinition definition = QuartzJobDefinition.CLASS_STATUS;

        JobDetail jobDetail = JobBuilder.newJob(UpdateClassesStatusJob.class)
                .withIdentity(definition.getJobName(), definition.getGroupName())
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(definition.getTriggerName(), definition.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();

        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scheduleProcessRecurringSurveyStatus(String cronExpression) {

        QuartzJobDefinition definition = QuartzJobDefinition.RECURRING_SURVEY_STATUS;

        JobDetail jobDetail = JobBuilder.newJob(ProcessRecurringSurveysJob.class)
                .withIdentity(definition.getJobName(), definition.getGroupName())
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(definition.getTriggerName(), definition.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();

        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scheduleJobs(QuartzJobDefinition def, String cronExpression) {
        JobDetail jobDetail = JobBuilder.newJob(resolveJobClass(def))
                .withIdentity(def.getJobName(), def.getGroupName())
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(def.getTriggerName(), def.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();

        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<? extends Job> resolveJobClass(QuartzJobDefinition def) {
        return switch (def) {
            case SLOT_STATUS -> UpdateSlotsStatusJob.class;
            case APPOINTMENT_STATUS -> UpdateAppointmentsStatusJob.class;
            case CLASS_STATUS -> UpdateClassesStatusJob.class;
            case SURVEY_STATUS -> UpdateSurveysStatusJob.class;
            case RECURRING_SURVEY_STATUS -> ProcessRecurringSurveysJob.class;
        };
    }



    @Override
    public List<Map<String, Object>> getAllScheduledJobs() {

        List<Map<String, Object>> jobsInfo = new ArrayList<>();

        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger : triggers) {
                        Map<String, Object> jobMap = new HashMap<>();
                        jobMap.put("jobName", jobKey.getName());
                        jobMap.put("group", jobKey.getGroup());
                        jobMap.put("triggerName", trigger.getKey().getName());

                        if (trigger instanceof CronTrigger cronTrigger) {
                            String description = CronUtils.describeCronExpression(cronTrigger.getCronExpression());
                            jobMap.put("cron", cronTrigger.getCronExpression());
                            jobMap.put("cronDescription", description);
                        } else {
                            jobMap.put("cron", "Not a cron trigger");
                        }

                        jobMap.put("nextFireTime", trigger.getNextFireTime());
                        jobMap.put("previousFireTime", trigger.getPreviousFireTime());

                        jobsInfo.add(jobMap);
                    }
                }
            }
            return jobsInfo;
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }
}
