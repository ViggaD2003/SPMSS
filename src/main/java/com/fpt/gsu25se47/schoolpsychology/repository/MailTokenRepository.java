package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.MailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailTokenRepository extends JpaRepository<MailToken, Integer> {

    Optional<MailToken> findByToken(String token);
}
