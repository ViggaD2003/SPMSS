package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateSlotsStatusJob implements Job {

    private final SlotRepository slotRepository;

    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime cutoff = LocalDateTime.now();
        log.info("Starting SlotCleanupJob at {}", cutoff);
        List<Slot> slots = slotRepository.findAllSlotsExpired(cutoff);
        log.info("Found {} expired slots to close", slots.size());
        slots.forEach(s -> {
            log.info("Closing slot with id={} (current status={}, start date = {}, end date = {})",
                    s.getId(), s.getStatus(), s.getStartDateTime(), s.getEndDateTime());
            s.setStatus(SlotStatus.CLOSED);
            slotRepository.save(s);
            log.info("Slot with id={} updated to CLOSED", s.getId());
        });
    }
}
