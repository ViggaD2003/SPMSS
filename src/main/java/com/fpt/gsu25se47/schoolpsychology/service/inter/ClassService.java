package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;

import java.util.List;

public interface ClassService {
    ClassDto createClass(ClassRequest request);
    ClassDto updateClass(String code, ClassRequest request);
    ClassDto getClassByCode(String code);
    List<ClassDto> getAllClasses();
    void deleteClassByCode(String code);
}
