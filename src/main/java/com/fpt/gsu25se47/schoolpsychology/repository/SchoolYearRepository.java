package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, Integer> {

    boolean existsByName(String name);

    @Query("SELECT sy FROM SchoolYear sy WHERE " +
            "(sy.startDate BETWEEN :startDate AND :endDate) OR " +
            "(sy.endDate BETWEEN :startDate AND :endDate) OR " +
            "(sy.startDate <= :startDate AND sy.endDate >= :endDate)")
    List<SchoolYear> findOverlappingSchoolYears(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);
}
