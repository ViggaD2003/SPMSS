package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.jobs.enums.QuartzJobDefinition;
import com.fpt.gsu25se47.schoolpsychology.service.inter.QuartzSchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QuartzSchedulerService quartzSchedulerService;

    @Operation(summary = "Update thời gian cập nhật trang thái tự động của models",
    description = """
            definition: các models cần update thời gian check và cập nhật tự động\n
            cronJobExpression: khoảng thời gian kích hoạt cập nhật tự động: \n
            \thttps://crontab.guru/examples.html
            \thttps://www.freeformatter.com/cron-expression-generator-quartz.html
            \tExamples:
            \tEvery second: * * * ? * *
            \tEvery minute: 0 * * ? * *
            \tEvery 2 minutes: 0 */2 * ? * *
            \tEvery 30 minutes: 0 */30 * ? * *
            \tEvery hour at minutes 15, 30 and 45: 0 15,30,45 * ? * *
            \tEvery hour: 0 0 * ? * *
            \tEvery six hours: 0 0 */6 ? * *
            \tEvery day at midnight - 12am: 0 0 0 * * ?
            \tEvery Sunday at noon: 0 0 12 ? * SUN
            \t...
            """)
    @PutMapping("/scheduler/slot-status")
    ResponseEntity<String> updateSlotStatusSchedule(@RequestParam QuartzJobDefinition definition,
                                                    @RequestParam String cronJobExpression) {

        quartzSchedulerService.scheduleJobs(definition, cronJobExpression);

        return ResponseEntity.ok("Update status cron time successfully");
    }

    @GetMapping("/scheduler/jobs")
    ResponseEntity<?> getAllScheduledJobs() {

        return ResponseEntity.ok(quartzSchedulerService.getAllScheduledJobs());
    }
}
