package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewLevelDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SubTypeRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SubTypeResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Level;
import com.fpt.gsu25se47.schoolpsychology.model.SubType;
import com.fpt.gsu25se47.schoolpsychology.repository.SubTypeRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SubTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubTypeServiceImpl implements SubTypeService {

    private final SubTypeRepository subTypeRepository;

    @Override
    public Optional<?> getAllSubTypes() {
        try {
            List<SubType> subTypes = subTypeRepository.findAll();
            List<SubTypeResponse> subTypeResponses = subTypes.stream()
                    .map(this::mapToResponse).toList();

            return Optional.of(subTypeResponses);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Something went wrong");
        }

    }

    @Override
    public Optional<?> addNewSubType(SubTypeRequest subTypeRequest) {
        if(subTypeRequest == null) {
            throw new IllegalArgumentException("Something went wrong");
        }

        SubType subType = this.mapToSubType(subTypeRequest);

        subType.getLevels().forEach(level -> {
            level.setSubType(subType);
        });

        subTypeRepository.save(subType);
        return Optional.of("SubType added successfully");
    }

    private SubType mapToSubType(SubTypeRequest subTypeRequest) {
        return SubType.builder()
                .description(subTypeRequest.getDescription())
                .codeName(subTypeRequest.getCodeName())
                .limitedQuestions(subTypeRequest.getLimitedQuestions())
                .length(subTypeRequest.getLength())
                .levels(subTypeRequest.getAddNewLevels().stream()
                        .map(this::mapToLevel).toList())
                .build();
    }

    private Level mapToLevel(AddNewLevelDto addNewLevelDto) {
        return Level.builder()
                .label(addNewLevelDto.getLabel())
                .maxScore(addNewLevelDto.getMaxScore())
                .minScore(addNewLevelDto.getMinScore())
                .levelName(addNewLevelDto.getLevelName())
                .build();
    }

    private SubTypeResponse mapToResponse(SubType subType) {
        return SubTypeResponse.builder()
                .id(subType.getId())
                .codeName(subType.getCodeName())
                .build();
    }
}
