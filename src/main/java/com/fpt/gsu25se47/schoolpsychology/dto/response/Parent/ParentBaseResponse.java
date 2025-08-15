package com.fpt.gsu25se47.schoolpsychology.dto.response.Parent;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import lombok.Data;

@Data
public class ParentBaseResponse extends AccountDto {

    private String address;
}
