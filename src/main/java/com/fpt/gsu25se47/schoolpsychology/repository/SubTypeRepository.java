package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SubTypeRepository extends JpaRepository<SubType, Integer> {

    @Query("SELECT st FROM SubType st WHERE st.codeName = :codeName")
    SubType findByCodeName(String codeName);
}
