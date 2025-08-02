package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CaseRepository extends JpaRepository<Cases, Integer> {
    @Query(value = """
    SELECT c.*
    FROM cases c
    LEFT JOIN accounts counselor ON c.counselor_id = counselor.id
    LEFT JOIN accounts teacher ON c.create_by = teacher.id
    LEFT JOIN accounts student ON c.student_id = student.id
    LEFT JOIN levels l ON c.initial_level_id = l.id
    WHERE 
        (
            (:role = 'MANAGER')
            OR (:role = 'COUNSELOR' AND counselor.id = :accountId)
            OR (:role = 'TEACHER' AND teacher.id = :accountId)
            OR (:role = 'STUDENT' AND student.id = :accountId)
        )
        AND (
            :statusCount = 0 OR c.status IN (:statuses)
        )
        AND (
            :categoryId IS NULL OR l.category_id = :categoryId
        )
    ORDER BY 
        FIELD(c.status, 'IN_PROGRESS', 'NEW', 'CLOSED')
    """, nativeQuery = true)
    List<Cases> findAllCasesByRoleAndAccountWithStatusSorted(
            @Param("role") String role,
            @Param("accountId") Integer accountId,
            @Param("statuses") List<String> statuses,
            @Param("statusCount") int statusCount,
            @Param("categoryId") Integer categoryId
    );



    @Query("SELECT COUNT(c) = 0 FROM Cases c WHERE c.student.id = :studentId AND c.status <> 'CLOSE'")
    boolean isStudentFreeFromOpenCases(@Param("studentId") Integer studentId);

    @Query(value = """
            SELECT ca.*
            FROM cases ca
            LEFT JOIN levels l ON ca.current_level_id = l.id
            WHERE l.category_id = :categoryId
            """
            , nativeQuery = true)
    List<Cases> findAllByCategoryId(Integer categoryId);

    @Query("SELECT COUNT(c) > 0 FROM Cases c WHERE c.student.id = :studentId AND c.status <> 'CLOSED'")
    boolean existsByStudentId(@Param("studentId") Integer studentId);
}
