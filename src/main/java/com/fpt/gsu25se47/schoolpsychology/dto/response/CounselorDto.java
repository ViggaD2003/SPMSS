package com.fpt.gsu25se47.schoolpsychology.dto.response;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorDto extends AccountDto{

    private String counselorCode;

    private String linkMeet;
}
