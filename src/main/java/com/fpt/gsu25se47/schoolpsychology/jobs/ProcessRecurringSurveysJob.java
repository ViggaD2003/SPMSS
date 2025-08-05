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
public class ProcessRecurringSurveysJob implements Job {

    private final SurveyRepository surveyRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Survey> recurringSurveys = surveyRepository.findAllRecurringSurveys();

        LocalDate today = LocalDate.now();

        for (Survey survey : recurringSurveys) {
            if (shouldOpen(survey, today)) {
                survey.setStartDate(today);
                int numberDoSurvey = survey.getEndDate().getDayOfMonth() - survey.getStartDate().getDayOfMonth();
                survey.setEndDate(today.plusDays(numberDoSurvey));
                survey.setStatus(SurveyStatus.PUBLISHED);
                survey.setRound(survey.getRound() + 1);
                surveyRepository.save(survey);
                System.out.println("Recurring survey " + survey.getId() + " has been published");
            }
        }
    }

    private boolean shouldOpen(Survey survey, LocalDate today) {
        if (!survey.getIsRecurring()) return false;
        if (survey.getStartDate() == null) return true;

        return switch (survey.getRecurringCycle()) {
            case WEEKLY -> !survey.getStartDate().plusWeeks(1).isAfter(today);
            case MONTHLY -> !survey.getStartDate().plusMonths(1).isAfter(today);
            default -> false;
        };
    }
}
