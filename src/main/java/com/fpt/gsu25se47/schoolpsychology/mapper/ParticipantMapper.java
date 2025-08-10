package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
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
            @Mapping(target = "surveyRecord", expression = "java(mapToSurveyRecordGetAllResponse(programParticipants.getStudent().getId(), programParticipants.getProgram().getSurvey().getId()))")
    })
    public abstract SupportProgramStudent mapToSupportProgramStudent(ProgramParticipants programParticipants);

    protected AccountDto mapStudent(Account student) {
        if (student == null) return null;
        return accountMapper.toDto(student);
    }

    protected List<SurveyRecordGetAllResponse> mapToSurveyRecordGetAllResponse(Integer studentId, Integer surveyId) {
        return surveyRecordRepository.findAllByStudentIdAndSurveyId(studentId, surveyId)
                .stream().map(surveyRecordMapper::mapToSurveyRecordGetAllResponse).toList();
    }

    protected CaseGetAllResponse mapToCase(Cases cases) {
        if(cases == null) return null;
        return caseMapper.mapToCaseGetAllResponse(cases, null);
    }
}
