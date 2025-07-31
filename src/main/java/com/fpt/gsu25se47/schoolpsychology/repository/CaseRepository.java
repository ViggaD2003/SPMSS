package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CaseRepository extends JpaRepository<Cases, Integer> {
    @Query("SELECT c FROM Cases c WHERE c.counselor.id =:counselorId")
    List<Cases> findAllByCounselorId(Integer counselorId);

    @Query("SELECT c FROM Cases c WHERE c.createBy.id =:teacherId")
    List<Cases> findAllByTeacherId(Integer teacherId);

    @Query("SELECT c FROM Cases c WHERE c.student.id =:studentId")
    List<Cases> findAllByStudentId(Integer studentId);

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

}
