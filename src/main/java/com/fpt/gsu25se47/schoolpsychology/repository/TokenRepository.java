package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByValue(String value);

    List<Token> findAllByAccount_Id(int id);

    Optional<Token> findByValueAndStatus(String value, String status);

    List<Token> findAllByTokenTypeAndStatusAndAccount_Id(String type, String status, int accountId);


}
