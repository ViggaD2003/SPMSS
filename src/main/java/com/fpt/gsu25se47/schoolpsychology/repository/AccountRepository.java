package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("""
        SELECT DISTINCT s.hostedBy
        FROM Slot s
        WHERE s.hostedBy.role = 'COUNSELOR'
    """)
    List<Account> findCounselorsWithSlots();

    @Query(value = """
        SELECT DISTINCT a.*
        FROM accounts a
        LEFT JOIN students s ON a.id = s.id AND a.role = 'STUDENT'
        LEFT JOIN teachers t ON a.id = t.id AND a.role = 'TEACHER'
        LEFT JOIN counselors co ON a.id = co.id AND a.role = 'COUNSELOR'
        LEFT JOIN guardians g ON a.id = g.id AND a.role = 'PARENTS'
        LEFT JOIN enrollment e ON s.id = e.student_id
        LEFT JOIN classes c ON (e.class_id = c.id OR t.id = c.teacher_id) AND c.is_active = 1
        WHERE a.status = 1
          AND (
            -- Trường hợp 1: Không truyền gì - view tất cả
            (:role IS NULL AND :classId IS NULL)
            OR
            -- Trường hợp 2: Role STUDENT/TEACHER có classId - view theo class
            (:role IN ('STUDENT', 'TEACHER') AND :classId IS NOT NULL
             AND a.role = :role AND c.id = :classId)
            OR
            -- Trường hợp 2: Role STUDENT/TEACHER không có classId - view all theo role
            (:role IN ('STUDENT', 'TEACHER') AND :classId IS NULL AND a.role = :role)
            OR
            -- Trường hợp 3: Role COUNSELOR/PARENTS - view all theo role
            (:role IN ('COUNSELOR', 'PARENTS') AND a.role = :role)
          )
        ORDER BY a.full_name ASC
        """, nativeQuery = true)
    List<Account> findAccountsByRoleNative(
            @Param("role") String role,
            @Param("classId") Integer classId
    );


}
