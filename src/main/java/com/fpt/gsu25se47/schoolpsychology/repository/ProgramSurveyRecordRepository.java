package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProgramSurveyRecordRepository extends JpaRepository<ProgramRecord, Integer> {

    @Query("SELECT pr FROM ProgramRecord pr WHERE pr.programSurvey.id = :programSurveyId and pr.programRegistration.id = :registrationId")
    Optional<List<ProgramRecord>> findBySupportProgramAndRegistration(Integer programSurveyId, Integer registrationId);
}
