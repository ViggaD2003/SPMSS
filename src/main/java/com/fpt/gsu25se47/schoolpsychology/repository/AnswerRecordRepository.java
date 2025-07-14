package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRecordRepository extends JpaRepository<AnswerRecord, Integer> {
}
