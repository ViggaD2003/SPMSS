package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.*;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.Teacher.TeacherDashboardResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher.AlertSkippedSurveyMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher.CaseSummDetailMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher.CaseSummaryMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher.StudentSkippedSurveyMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.CaseRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.TeacherDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherDashboardServiceImpl implements TeacherDashboardService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final CaseRepository caseRepository;
    private final AccountService accountService;
    private final CaseSummDetailMapper caseSummDetailMapper;
    private final CaseSummaryMapper caseSummaryMapper;
    private final AlertSkippedSurveyMapper alertSkippedSurveyMapper;
    private final StudentSkippedSurveyMapper studentSkippedSurveyMapper;

    @Override
    public TeacherDashboardResponse getTeacherDashboardResponse() {

        Account account = accountService.getCurrentAccount();

        Classes currentClass = account.getTeacher().getClasses()
                .stream()
                .filter(Classes::getIsActive)
                .findFirst()
                .orElse(null);

        assert currentClass != null;

        List<Account> students = currentClass.getEnrollments()
                .stream()
                .map(Enrollment::getStudent)
                .map(Student::getAccount)
                .toList();

        OverviewResponse overviewResponse = getOverviewResponse(students);

        List<CaseSummDetailResponse> caseSummDetailResponses = getCaseSummDetailResponses(students);

        List<CaseSummaryResponse> caseSummaryResponses = getCaseSummaryResponses(students);

        List<StudentSkippedSurveyResponse> studentSkippedSurveyResponses = getStudentSkippedSurveyResponses(students);

        int studentsCount = (int) students.stream()
                .map(a -> surveyRecordRepository.findSkippedSurveyRecordsByStudentIdInCurrentMonth(a.getId()))
                .filter(list -> !list.isEmpty())
                .count();

        AlertSkippedSurveyResponse alertSkippedSurveyResponse = alertSkippedSurveyMapper.toAlertSkippedSurveyResponse(studentSkippedSurveyResponses,
                studentsCount);

        return TeacherDashboardResponse.builder()
                .overview(overviewResponse)
                .cases(caseSummDetailResponses)
                .caseSummary(caseSummaryResponses)
                .alertSkippedSurveys(alertSkippedSurveyResponse)
                .build();
    }

    private List<StudentSkippedSurveyResponse> getStudentSkippedSurveyResponses(List<Account> students) {
        return students.stream()
                .map(acc -> {
                    List<SurveyRecord> surveyRecordsSkippedThisMonth = surveyRecordRepository.findSkippedSurveyRecordsByStudentIdInCurrentMonth(acc.getId());

                    if (surveyRecordsSkippedThisMonth == null || surveyRecordsSkippedThisMonth.isEmpty()) {
                        return null;
                    }

                    List<String> surveyTitles = surveyRecordsSkippedThisMonth
                            .stream()
                            .map(sr -> sr.getSurvey().getTitle())
                            .toList();

                    return new StudentSkippedSurveyResponse(acc.getFullName(), surveyTitles);
                })
                .toList();
    }

    private List<CaseSummaryResponse> getCaseSummaryResponses(List<Account> students) {
        List<CaseSummaryResponse> caseSummaryResponses = students.stream()
                .flatMap(a -> a.getStudentCases()
                        .stream()
                        .map((c) -> {
                            int count = (int) caseRepository.countCasesWithLevelId(c.getCurrentLevel().getId());
                            return caseSummaryMapper.toCaseSummaryResponse(c, count);
                        }))
                .toList();
        return caseSummaryResponses;
    }

    private List<CaseSummDetailResponse> getCaseSummDetailResponses(List<Account> students) {
        List<CaseSummDetailResponse> caseSummDetailResponses = students.stream()
                .flatMap(a -> a.getStudentCases()
                        .stream()
                        .map(caseSummDetailMapper::toCaseSummDetailResponse))
                .toList();
        return caseSummDetailResponses;
    }

    private static OverviewResponse getOverviewResponse(List<Account> students) {
        Integer totalStudents = (int) students.stream()
                .map(e -> e.getStudent().getId())
                .distinct()
                .count();

        Integer studentsWithCases = (int) students.stream()
                .map(Account::getStudentCases)
                .filter(c -> !c.isEmpty())
                .count();

        Integer studentsInPrograms = (int) students.stream()
                .map(Account::getProgramRegistrations)
                .filter(pr -> !pr.isEmpty())
                .count();

        int studentsCompletedSurveys = (int) students.stream()
                .map(Account::getSurveyRecords)
                .filter(sr -> sr != null && sr.stream().anyMatch(s -> !s.getIsSkipped()))
                .count();

        OverviewResponse overviewResponse = OverviewResponse.builder()
                .totalStudents(totalStudents)
                .studentsCompletedSurveys(studentsCompletedSurveys)
                .studentsWithCases(studentsWithCases)
                .studentsInPrograms(studentsInPrograms)
                .build();
        return overviewResponse;
    }
}
