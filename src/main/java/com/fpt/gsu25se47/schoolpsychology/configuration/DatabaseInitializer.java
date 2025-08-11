package com.fpt.gsu25se47.schoolpsychology.configuration;

import com.fpt.gsu25se47.schoolpsychology.jobs.enums.QuartzJobDefinition;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.QuartzSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private QuartzSchedulerService quartzSchedulerService;

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            Resource resource = resourceLoader.getResource("classpath:data.sql");
            System.out.println(resource.getFilename());
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, resource);
            }
        }

        Scheduler scheduler = quartzSchedulerService.getScheduler();
        scheduler.start();
        log.info("✅ Quartz Scheduler started manually");
        for (QuartzJobDefinition def : QuartzJobDefinition.values()) {
            try {
                String defaultCron = "0 0 0 * * ?";
                JobKey jobKey = new JobKey(def.getJobName(), def.getGroupName());

                if (!scheduler.checkExists(jobKey)) {
                    quartzSchedulerService.scheduleJobs(def, defaultCron);
                    log.info("Scheduled job '{}' with default cron: {}", def.getJobName(), defaultCron);
                } else {
                    TriggerKey triggerKey = new TriggerKey(def.getTriggerName(), def.getGroupName());
                    CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    String existCron = cronTrigger.getCronExpression();

                    if (existCron == null || existCron.isBlank()) {
                        existCron = defaultCron;
                    }

                    if (!existCron.equals(defaultCron)) {
                        Trigger newTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(triggerKey)
                                .withSchedule(CronScheduleBuilder.cronSchedule(existCron))
                                .build();

                        scheduler.rescheduleJob(triggerKey, newTrigger);
                        log.info("Rescheduled job '{}' with new cron: {}", def.getJobName(), existCron);
                    }
                }
            } catch (SchedulerException e) {
                log.error("Failed to schedule job '{}': {}", def.getJobName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}