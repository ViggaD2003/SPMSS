package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateSurveysStatusJob implements Job {

    private final SurveyRepository surveyRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDate now = LocalDate.now();

        List<Survey> toPublish = surveyRepository.findByStartDateAndStatusDraft(now);
        toPublish.forEach(survey -> survey.setStatus(SurveyStatus.PUBLISHED));

        List<Survey> toFinish = surveyRepository.findByEndDateAndStatusPublished(now);
        toFinish.forEach(survey -> survey.setStatus(SurveyStatus.ARCHIVED));

        surveyRepository.saveAll(toPublish);
        surveyRepository.saveAll(toFinish);
    }
}
