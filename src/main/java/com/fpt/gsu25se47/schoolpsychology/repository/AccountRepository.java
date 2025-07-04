package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
