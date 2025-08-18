package com.fpt.gsu25se47.schoolpsychology.dto.response;


import lombok.*;

import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorDto extends AccountDto{

    private String counselorCode;

    private String linkMeet;

    private Set<Integer> categories;

    private Boolean hasAvailable;
}
