package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;

import java.util.List;

public interface ClassService {
    ClassResponse createClass(CreateClassRequest request);
//    ClassResponse updateClass(String code, ClassRequest request);
//    ClassResponse getClassByCode(String code);
//    List<ClassResponse> getAllClasses();
//    void deleteClassByCode(String code);
}
