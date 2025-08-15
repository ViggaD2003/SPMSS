package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Integer> {

    @Query("""
            SELECT t FROM Term t
            WHERE t.schoolYear.id = :yearId
            """)
    List<Term> findAllByYearId(Integer yearId);
}
