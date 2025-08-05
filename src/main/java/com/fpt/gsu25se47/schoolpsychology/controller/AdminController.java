package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.jobs.enums.QuartzJobDefinition;
import com.fpt.gsu25se47.schoolpsychology.model.SystemConfig;
import com.fpt.gsu25se47.schoolpsychology.service.inter.QuartzSchedulerService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QuartzSchedulerService quartzSchedulerService;
    private final SystemConfigService systemConfigService;

    @PreAuthorize("hasRole('MANAGER')")
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

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Lấy tất cả các tác vụ cập nhật thời gian tự động")
    @GetMapping("/scheduler/jobs")
    ResponseEntity<?> getAllScheduledJobs() {

        return ResponseEntity.ok(quartzSchedulerService.getAllScheduledJobs());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Lấy tất cả các config của hệ thống")
    @GetMapping("/system/configs")
    ResponseEntity<List<SystemConfig>> getAllSystemConfigs() {

        return ResponseEntity.ok(systemConfigService.getAllConfigs());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Lấy tất cả các config của hệ thống theo groupName",
    description = """
            Hiện tại groupName đang có trong hệ thống là : APPOINTMENT, SURVEY, SUPPORT_PROGRAM
            """)
    @GetMapping("/system/configs/groups")
    ResponseEntity<List<SystemConfig>> getAllByGroup(@RequestParam String groupName) {

        return ResponseEntity.ok(systemConfigService.getConfigsByGroup(groupName));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Cập nhật value của key trong hệ thống",
    description = """
            Ví dụ: Cập nhật tính năng cho phép tạo appointment\n
            Trong system config có key: appointment.enabled, value: false or true\n
            Cập nhật value cho phép tính năng tạo appointment hoạt động:\n
            configKey: appointment.enabled, configValue: true""")
    @PutMapping("/system/configs")
    ResponseEntity<SystemConfig> updateValue(@RequestParam String configKey,
                                             @RequestParam String configValue) {

        return ResponseEntity.ok(systemConfigService.updateConfigValue(configKey, configValue));
    }
}
