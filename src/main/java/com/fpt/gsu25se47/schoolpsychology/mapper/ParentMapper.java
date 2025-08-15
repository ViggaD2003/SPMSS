package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Parent.ParentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.RelationshipDto;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Guardian;
import com.fpt.gsu25se47.schoolpsychology.model.Relationship;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public abstract class ParentMapper {

    @Autowired
    protected StudentMapper studentMapper;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassMapper classMapper;

    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "relationships", ignore = true) // xử lý thủ công phần này
    public abstract ParentDto toDto(Guardian parent);

    public ParentDto toParentWithRelationshipDto(Guardian parent) {
        ParentDto dto = toDto(parent);

        RelationshipDto relationshipDto = new RelationshipDto();

        if (parent.getRelationships() != null) {
            relationshipDto.setStudent(
                    parent.getRelationships().stream()
                            .map(Relationship::getStudent)
                            .map(st -> {
                                Classes activeClass = classRepository.findActiveClassByStudentId(st.getId());
                                return studentMapper.mapStudentDtoWithClass(st, activeClass, classMapper);
                            }) // hoặc toAccountDto nếu bạn có
                            .toList()
            );
        }

        dto.setRelationships(relationshipDto);
        return dto;
    }
}

