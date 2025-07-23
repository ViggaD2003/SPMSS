package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LevelRepository extends JpaRepository<Level, Integer> {

    @Query("SELECT l FROM Level l WHERE l.category.id =:categoryId")
    List<Level> findAllByCategoryId(Integer categoryId);
}
