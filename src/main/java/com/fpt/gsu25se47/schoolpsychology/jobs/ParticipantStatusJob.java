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
public class  ParticipantStatusJob implements Job {

    private final ProgramParticipantRepository participantRepository;

    private final SurveyRecordRepository surveyRecordRepository;

    private final SupportProgramRepository supportProgramRepository;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();

        List<SupportProgram> listSupportProgram = supportProgramRepository.findAll();

        listSupportProgram.forEach(program -> {
            program.getProgramRegistrations().forEach(registration -> {
                if((program.getStartTime() == now) &&
                        !surveyRecordRepository.isEntrySurveyRecordByStudentId(registration.getStudent().getId(), program.getId())){
                    registration.setFinalScore(0f);
                    registration.setStatus(RegistrationStatus.ABSENT);
                    participantRepository.save(registration);
                } else if((program.getEndTime() == now) &&
                        !surveyRecordRepository.isExitSurveyRecordByStudentId(registration.getStudent().getId(), program.getId())){
                    registration.setFinalScore(0f);
                    registration.setStatus(RegistrationStatus.ABSENT);
                    participantRepository.save(registration);
                }
            });
        });
    }
}
