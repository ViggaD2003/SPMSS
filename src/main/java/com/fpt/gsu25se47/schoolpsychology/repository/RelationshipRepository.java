package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Relationship;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
    @Query("SELECT r.student FROM Relationship r WHERE r.guardian.account.id = :parentAccountId")
    List<Student> findChildrenByParentAccountId(@Param("parentAccountId") Integer parentAccountId);
}
