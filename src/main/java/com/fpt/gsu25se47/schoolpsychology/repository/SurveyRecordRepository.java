package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRecordRepository extends JpaRepository<SurveyRecord, Integer> {

    Page<SurveyRecord> findAllByStudentId(int accountId, Pageable pageable);
}
