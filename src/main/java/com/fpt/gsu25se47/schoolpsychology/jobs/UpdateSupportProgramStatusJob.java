package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.SupportProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateSupportProgramStatusJob implements Job {

    private final SupportProgramRepository supportProgramRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();
        log.info("Test cronjob support program status updated at {}", now);
        List<SupportProgram> supportProgramsStart = supportProgramRepository.findAll()
                .stream().filter(item -> item.getStartTime() == now)
                .toList();

        supportProgramsStart.forEach(program -> program.setStatus(ProgramStatus.ON_GOING));

        List<SupportProgram> supportProgramsEnd = supportProgramRepository.findAll()
                .stream().filter(item -> item.getEndTime() == now)
                .toList();

        supportProgramsEnd.forEach(program -> program.setStatus(ProgramStatus.COMPLETED));
    }
}
