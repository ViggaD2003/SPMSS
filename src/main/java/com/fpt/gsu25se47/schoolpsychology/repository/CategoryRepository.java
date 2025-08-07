package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.CaseSummaryResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCode(String code);

    Optional<Category> findByName(String name);

    @Query("""
                SELECT new com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.CaseSummaryResponse(
                    c.id,
                    c.name,
                    c.code,
                    COUNT(DISTINCT cs.id)
                )
                FROM Category c
                JOIN c.levels l
                JOIN l.levelOfCurrentCase cs
                JOIN cs.student a
                JOIN Student s ON s.id = a.id
                JOIN s.enrollments e
                JOIN e.classes cl
                JOIN cl.teacher t
                WHERE t.id = :teacherId
                  AND cl.isActive = true
                GROUP BY c.id, c.name
            """)
    List<CaseSummaryResponse> getCaseCountsByCategoryForCurrentTeacher(@Param("teacherId") Integer teacherId);

}
