package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponseSRC;

import java.util.List;

public interface ClassService {
    List<ClassResponse> createClass(List<CreateClassRequest> request);
    ClassResponse updateClass(Integer classId, UpdateClassRequest request);
    ClassResponseSRC getClassByCode(String code);
    ClassResponseSRC getClassById(Integer classId);
    List<ClassResponse> getAllClasses();
//    void deleteClassByCode(String code);
}
