package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateClassesStatusJob implements Job {

    private final ClassRepository classRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime cutoff = LocalDateTime.now();

        List<Classes> classes = classRepository.findAllExpiredClasses(cutoff);

        classes.forEach(c -> {
            c.setIsActive(false);
            classRepository.save(c);
        });
    }
}
