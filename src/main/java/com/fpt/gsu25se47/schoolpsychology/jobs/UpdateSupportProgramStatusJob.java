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
        List<SupportProgram> programs = supportProgramRepository.findAll();

        programs.forEach(program -> {
            ProgramStatus oldStatus = program.getStatus();

            if (now.isAfter(program.getEndTime()) || now.isEqual(program.getEndTime())) {
                program.setStatus(ProgramStatus.COMPLETED);
            } else if (now.isAfter(program.getStartTime()) || now.isEqual(program.getStartTime())) {
                program.setStatus(ProgramStatus.ON_GOING);
            }

            if (program.getStatus() != oldStatus) {
                log.info("Program {} status updated from {} to {}", program.getId(), oldStatus, program.getStatus());
            }
        });

        supportProgramRepository.saveAll(programs);
    }

}
