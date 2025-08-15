package com.fpt.gsu25se47.schoolpsychology.dto.response.Parent;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.RelationshipDto;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDto extends AccountDto {

    private String address;

    private RelationshipDto relationships;

}
