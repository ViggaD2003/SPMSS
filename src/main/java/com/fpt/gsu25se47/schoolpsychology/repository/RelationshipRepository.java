package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Relationship;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
    @Query("SELECT r FROM Relationship r WHERE r.guardian.id = :parentId and r.student.id = :childId")
    Relationship findByParentIdAndChildId(@Param("parentId") Integer parentId, @Param("childId") Integer childId);

    @Query("SELECT COUNT(r) > 0 " +
            "FROM Relationship r " +
            "WHERE r.guardian.id = :parentId " +
            "AND r.student.id IN :childIds")
    boolean checkRelationshipExists(@Param("parentId") Integer parentId,
                                    @Param("childIds") List<Integer> childIds);

}
