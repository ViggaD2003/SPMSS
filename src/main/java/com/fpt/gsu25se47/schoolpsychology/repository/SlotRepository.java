//package com.fpt.gsu25se47.schoolpsychology.repository;
//
//import com.fpt.gsu25se47.schoolpsychology.model.Slot;
//import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public interface SlotRepository extends JpaRepository<Slot, Integer> {
//
//    @Query("SELECT s FROM Slot s WHERE s.hostedBy.id = :hostedById")
//    List<Slot> findAllByHostedById(Integer hostedById);
//
//    @Query("SELECT s FROM Slot s WHERE (s.status = :published OR s.status = :draft) AND s.endDateTime < :now")
//    List<Slot> findAllSlotsExpired(@Param("now") LocalDateTime now,
//                                   @Param("published") SlotStatus published,
//                                   @Param("draft") SlotStatus draft);
//
//
//    @Query("""
//                SELECT s FROM Slot s
//                WHERE s.hostedBy.id = :hostId
//                  AND DATE(s.startDateTime) = DATE(:newStart)
//                  AND (
//                        (:newStart < s.endDateTime AND :newEnd > s.startDateTime)
//                      )
//            """)
//    List<Slot> findConflictingSlots(
//            @Param("hostId") Integer hostId,
//            @Param("newStart") LocalDateTime newStart,
//            @Param("newEnd") LocalDateTime newEnd
//    );
//
//    @Query("""
//                SELECT s FROM Slot s
//                WHERE s.hostedBy.id = :hostId
//                  AND FUNCTION('DATE_FORMAT', s.startDateTime, '%Y-%m-%d %H:%i') =
//                      FUNCTION('DATE_FORMAT', :startDateTime, '%Y-%m-%d %H:%i')
//                  AND FUNCTION('DATE_FORMAT', s.endDateTime, '%Y-%m-%d %H:%i') =
//                      FUNCTION('DATE_FORMAT', :endDateTime, '%Y-%m-%d %H:%i')
//            """)
//    List<Slot> findExactSlotByStartAndEndMinute(
//            @Param("hostId") Integer hostId,
//            @Param("startDateTime") LocalDateTime startDateTime,
//            @Param("endDateTime") LocalDateTime endDateTime
//    );
//
//
//}
