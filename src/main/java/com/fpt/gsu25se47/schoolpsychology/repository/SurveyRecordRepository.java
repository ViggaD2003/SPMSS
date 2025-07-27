package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyRecordRepository extends JpaRepository<SurveyRecord, Integer> {

    @Query("SELECT sr FROM SurveyRecord sr " +
            "WHERE sr.student.id = :studentId " +
            "AND (:surveyType IS NULL OR sr.survey.surveyType = :surveyType)")
    Page<SurveyRecord> findAllSurveyRecordsByStudentIdAndSurveyType(@Param("studentId") int studentId,
                                                                    @Param("surveyType") SurveyType surveyType,
                                                                    Pageable pageable);

}
