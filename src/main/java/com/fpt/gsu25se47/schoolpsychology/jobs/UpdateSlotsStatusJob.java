package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateSlotsStatusJob implements Job {

    private final SlotRepository slotRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime cutoff = LocalDateTime.now();

        List<Slot> slots = slotRepository.findAllSlotsExpired(cutoff, SlotStatus.PUBLISHED, SlotStatus.DRAFT);

        slots.forEach(s -> {
            s.setStatus(SlotStatus.CLOSED);
            slotRepository.save(s);
        });
    }
}
