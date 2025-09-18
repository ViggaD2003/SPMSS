package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.MailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MailTokenRepository extends JpaRepository<MailToken, Integer> {

    Optional<MailToken> findByToken(String token);

    @Query("SELECT mt FROM MailToken mt WHERE mt.revoked = false and mt.account.id =:accountId")
    List<MailToken> findAllWhereRevokedIsFalse(Integer accountId);
}
