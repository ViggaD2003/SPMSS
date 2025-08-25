package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordIdentify;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordType;
import com.fpt.gsu25se47.schoolpsychology.repository.MentalEvaluationRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ParticipantMapper {

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected CaseMapper caseMapper;

    @Autowired
    protected SurveyRecordMapper surveyRecordMapper;
    @Autowired
    private SurveyRecordRepository surveyRecordRepository;
    @Autowired
    private MentalEvaluationMapper mentalEvaluationMapper;
    @Autowired
    private MentalEvaluationRepository mentalEvaluationRepository;

    @Mappings({
            @Mapping(target = "student", expression = "java(mapStudent(programParticipants.getStudent()))"),
            @Mapping(target = "cases", expression = "java(mapToCase(programParticipants.getCases()))")
    })
    public abstract ProgramParticipantsResponse mapToDto(ProgramParticipants programParticipants);


    @Mappings({
            @Mapping(target = "participantId", source = "programParticipants.id"),
            @Mapping(target = "studentId", source = "programParticipants.student.id"),
            @Mapping(target = "caseId", source = "programParticipants.cases.id"),
            @Mapping(target = "status", expression = "java(programParticipants.getStatus().name())"),
            @Mapping(target = "surveyRecord", expression = "java(mapToSurveyRecordGetAllResponse(programParticipants.getStudent().getId(), programParticipants.getProgram().getSurvey().getId()))"),
            @Mapping(target = "mentalEvaluation", expression = "java(mapToMentalEvaluationResponse(programParticipants.getId()))")
    })
    public abstract SupportProgramStudent mapToSupportProgramStudent(ProgramParticipants programParticipants);

    @Mapping(target = "registrationStatus", source = "status")
    @Mapping(target = "studentId", source = "student.id")
    public abstract ProgramPPParticipantResponse toProgramPPParticipantResponse(ProgramParticipants programParticipants);

    protected AccountDto mapStudent(Account student) {
        if (student == null) return null;
        return accountMapper.toDto(student);
    }

    protected List<SurveyRecordGetAllResponse> mapToSurveyRecordGetAllResponse(Integer studentId, Integer surveyId) {
        return surveyRecordRepository.findAllByStudentIdAndSurveyId(studentId, surveyId)
                .stream().map(surveyRecordMapper::mapToSurveyRecordGetAllResponse).toList();
    }

    protected CaseGetAllResponse mapToCase(Cases cases) {
        if (cases == null) return null;
        return caseMapper.mapToCaseGetAllResponse(cases, null);
    }

    protected MentalEvaluationResponse mapToMentalEvaluationResponse(Integer participantId) {
        return mentalEvaluationMapper.toMentalEvaluationResponse(mentalEvaluationRepository
                .findMentalEvaluationByProgramParticipantsId(participantId));
    }

    public ProgramParticipantsResponse mapToProgramParticipantsResponse(ProgramParticipants programParticipants) {
        ProgramParticipantsResponse response = mapToDto(programParticipants);

        List<SurveyRecord> surveyRecords = surveyRecordRepository.findTwoSurveyRecordsByParticipant(programParticipants.getId());

        surveyRecords.forEach(surveyRecord -> {
            if (surveyRecord.getSurveyRecordIdentify() == SurveyRecordIdentify.ENTRY && surveyRecord.getSurveyRecordType() == SurveyRecordType.PROGRAM) {
                response.setSurveyIn(surveyRecord.getTotalScore());
            } else {
                response.setSurveyOut(surveyRecord.getTotalScore());
            }
        });
        return response;
    }
}
