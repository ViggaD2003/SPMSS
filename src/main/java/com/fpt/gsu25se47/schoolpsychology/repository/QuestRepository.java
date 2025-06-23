package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Question, Integer> {
}
