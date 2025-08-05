//package com.fpt.gsu25se47.schoolpsychology.configuration;
//
//import com.fpt.gsu25se47.schoolpsychology.jobs.enums.QuartzJobDefinition;
//import com.fpt.gsu25se47.schoolpsychology.service.inter.QuartzSchedulerService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.JobKey;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class QuartzJobInitializer implements ApplicationListener<ContextRefreshedEvent> {
//
//    private final QuartzSchedulerService quartzSchedulerService;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//
//        Scheduler scheduler = quartzSchedulerService.getScheduler();
//
//        for (QuartzJobDefinition def : QuartzJobDefinition.values()) {
//            try {
//                JobKey jobKey = new JobKey(def.getJobName(), def.getGroupName());
//                if (!scheduler.checkExists(jobKey)) {
//                    String defaultCron = "0 0 */12 * * ?";
//                    quartzSchedulerService.scheduleJobs(def, defaultCron);
//                    log.info("Scheduled job '{}' with default cron: {}", def.getJobName(), defaultCron);
//                } else {
//                    log.info("Job '{}' already exists. Skipping default scheduling.", def.getJobName());
//                }
//            } catch (SchedulerException e) {
//                log.error("Failed to schedule job '{}': {}", def.getJobName(), e.getMessage());
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
