package com.fpt.gsu25se47.schoolpsychology.configuration;

import com.fpt.gsu25se47.schoolpsychology.jobs.enums.QuartzJobDefinition;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.QuartzSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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

                JobKey jobKey = new JobKey(def.getJobName(), def.getGroupName());
                if (!scheduler.checkExists(jobKey)) {
                    String defaultCron = "0 0 */12 * * ?";
                    quartzSchedulerService.scheduleJobs(def, defaultCron);
                    log.info("Scheduled job '{}' with default cron: {}", def.getJobName(), defaultCron);
                } else {
                    log.info("Job '{}' already exists. Skipping default scheduling.", def.getJobName());
                }
            } catch (SchedulerException e) {
                log.error("Failed to schedule job '{}': {}", def.getJobName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}