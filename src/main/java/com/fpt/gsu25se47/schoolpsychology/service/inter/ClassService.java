package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponseSRC;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;

import java.util.List;

public interface ClassService {
    List<ClassResponse> createClass(List<CreateClassRequest> request);
//    ClassResponse updateClass(String code, ClassRequest request);
    ClassResponseSRC getClassByCode(String code);
    ClassResponseSRC getClassById(Integer classId);
    List<ClassResponse> getAllClasses(Grade grade);
//    void deleteClassByCode(String code);
}
