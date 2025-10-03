package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.ProgramParticipantRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SupportProgramRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParticipantStatusJob implements Job {

    private final ProgramParticipantRepository participantRepository;
    private final SurveyRecordRepository surveyRecordRepository;
    private final SupportProgramRepository supportProgramRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();

        List<SupportProgram> programs = supportProgramRepository.findAll();

        programs.forEach(program -> {
            program.getProgramRegistrations().forEach(registration -> {
                Integer studentId = registration.getStudent().getId();
                Integer programId = program.getId();

                boolean hasEntry = surveyRecordRepository.isEntrySurveyRecordByStudentId(studentId, programId);
                boolean hasExit = surveyRecordRepository.isExitSurveyRecordByStudentId(studentId, programId);

                // Rule 1: Nếu đã qua thời gian bắt đầu mà chưa làm Entry Survey => ABSENT
                if (program.getStartTime().isBefore(now) && !hasEntry) {
                    registration.setFinalScore(0f);
                    registration.setStatus(RegistrationStatus.ABSENT);

                    // Rule 2: Nếu đã qua thời gian kết thúc mà không có Exit Survey => ABSENT
                } else if (program.getEndTime().isBefore(now) && hasEntry && !hasExit) {
                    registration.setFinalScore(0f);
                    registration.setStatus(RegistrationStatus.ABSENT);

                    // Rule 3: Nếu chương trình kết thúc mà người đó chỉ ở trạng thái ENROLLED => ABSENT
                } else if (program.getEndTime().isBefore(now) && (registration.getStatus().equals(RegistrationStatus.ENROLLED)|| registration.getStatus() == RegistrationStatus.ACTIVE)) {
                    registration.setFinalScore(0f);
                    registration.setStatus(RegistrationStatus.ABSENT);
                }

                participantRepository.save(registration);
            });
        });
    }
}
