package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.SupportProgramRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateSupportProgramStatusJob implements Job {

    private final SupportProgramRepository supportProgramRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();
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
