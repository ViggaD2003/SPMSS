package com.fpt.gsu25se47.schoolpsychology.dto.response.Parent;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDto extends AccountDto {

    private String address;

    private String relationshipType;

    private List<StudentDto> student;
}
