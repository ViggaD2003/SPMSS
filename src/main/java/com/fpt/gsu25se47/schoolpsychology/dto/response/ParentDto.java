package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDto extends AccountDto{

    private String address;

    private RelationshipDto relationships;

}
