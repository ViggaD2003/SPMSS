package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SupportProgramRepository extends JpaRepository<SupportProgram, Integer> {

//    @Query("""
//                SELECT sp FROM SupportProgram sp
//                WHERE (:status IS NULL OR sp.status = :status)
//                  AND (:categoryId IS NULL OR sp.category.id = :categoryId)
//                  AND (:isOnline IS NULL OR sp.isOnline = :isOnline)
//                  AND (:startDate IS NULL OR sp.startDate >= :startDate)
//                  AND (:endDate IS NULL OR sp.endDate <= :endDate)
//                  AND (:name IS NULL OR LOWER(sp.name) LIKE LOWER(CONCAT('%', :name, '%')))
//                  AND (:minParticipants IS NULL OR sp.maxParticipants >= :minParticipants)
//                  AND (:maxParticipants IS NULL OR sp.maxParticipants <= :maxParticipants)
//                ORDER BY sp.startDate DESC
//            """)
//    List<SupportProgram> getAllSupportPrograms(
//            @Param("status") ProgramStatus status,
//            @Param("categoryId") Integer categoryId,
//            @Param("isOnline") Boolean isOnline,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("name") String name,
//            @Param("minParticipants") Integer minParticipants,
//            @Param("maxParticipants") Integer maxParticipants
//    );

    @Query("SELECT sp FROM SupportProgram sp WHERE sp.hostedBy.id = :hostedById")
    List<SupportProgram> findAllByHostedBy(Integer hostedById);
}
