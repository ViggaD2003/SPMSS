package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Classes,Integer> {
//    Classes findByClassName(String className);
    Optional<Classes> findByCodeClass(String code);
}
