//package com.fpt.gsu25se47.schoolpsychology.service.impl;
//
//import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
//import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
//import com.fpt.gsu25se47.schoolpsychology.model.Category;
//import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
//import com.fpt.gsu25se47.schoolpsychology.repository.*;
//import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@RequiredArgsConstructor
//public class SupportProgramServiceImpl implements SupportProgramService {
//
//    private final SupportProgramMapper supportProgramMapper;
//    private final ProgramSessionRepository programSessionRepository;
//    private final ProgramRegistrationRepository programRegistrationRepository;
//    private final SupportProgramRepository supportProgramRepository;
//    private final ProgramSurveyRepository programSurveyRepository;
//    private final CategoryRepository categoryRepository;
//
//    @Override
//    @Transactional
//    public SupportProgramResponse createSupportProgram(SupportProgramRequest request) {
//
//        Category category = categoryRepository.findById(request.getCategoryId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
//                        "Category not found with ID: " + request.getCategoryId()));
//
////        if (request.getSessionIds().isEmpty()) {
////            supportProgram = supportProgramMapper.toSupportProgram(
////                    request,
////                    category,
////                    Collections.emptyList());
////        } else {
////            var sessions = programSessionRepository.findAllById(request.getSessionIds());
////            if (sessions.size() != request.getSessionIds().size()) {
////                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more session IDs are invalid.");
////            }
////
////            sessions.forEach(t -> {
////                if(t.getProgram() != null) {
////                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of sessions existed in another support program");
////                }
////                if(t.getDate().isBefore(request.getStartDate()) || t.getDate().isAfter(request.getEndDate())) {
////                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This session date is invalid by support program start date and end date");
////                }
////            });
//
//        SupportProgram supportProgram = supportProgramMapper.toSupportProgram(request,
//                category);
//
//        SupportProgram supportProgramCreated = supportProgramRepository.save(supportProgram);
//
//        return supportProgramMapper.toSupportProgramResponse(supportProgramCreated);
//    }
//
//    @Override
//    public SupportProgramResponse getSupportProgramById(Integer id) {
//
//        return supportProgramMapper.toSupportProgramResponse(
//                getSupportProgram(id)
//        );
//    }
//
//    @Override
//    public List<SupportProgramResponse> getAllSupportPrograms() {
//
////        List<SupportProgram> supportPrograms = supportProgramRepository.getAllSupportPrograms(
////                request.getStatus(),
////                request.getCategoryId(),
////                request.getIsOnline(),
////                request.getStartDate(),
////                request.getEndDate(),
////                request.getName(),
////                request.getMinParticipants(),
////                request.getMaxParticipants()
////        );
//
//        List<SupportProgram> supportPrograms = supportProgramRepository.findAll();
//
//        return supportPrograms
//                .stream()
//                .map(supportProgramMapper::toSupportProgramResponse)
//                .toList();
//    }
//
//    @Override
//    public void deleteSupportProgram(Integer id) {
//
//        SupportProgram supportProgram = getSupportProgram(id);
//
//        supportProgramRepository.delete(supportProgram);
//    }
//
//    @Override
//    public SupportProgramResponse updateSupportProgram(Integer id, SupportProgramRequest request) {
//
//        SupportProgram supportProgram = getSupportProgram(id);
//
//        if (!Objects.equals(request.getCategoryId(), supportProgram.getCategory().getId())) {
//            Category category = categoryRepository.findById(request.getCategoryId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
//                            "Category not found with ID: " + request.getCategoryId()));
//            supportProgram.setCategory(category);
//        }
//
//        supportProgramMapper.updateSupportProgramFromRequest(request, supportProgram);
//
//        SupportProgram supportProgramUpdated = supportProgramRepository.save(supportProgram);
//
//        return supportProgramMapper.toSupportProgramResponse(supportProgramUpdated);
//    }
//
//    private SupportProgram getSupportProgram(Integer id) {
//
//        return supportProgramRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND,
//                        "Support program not found with ID: " + id
//                ));
//    }
//}
