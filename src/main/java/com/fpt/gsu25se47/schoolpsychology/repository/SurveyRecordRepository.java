package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRecordRepository extends JpaRepository<SurveyRecord, Integer> {

    List<SurveyRecord> findAllByAccountId(int accountId);
}
