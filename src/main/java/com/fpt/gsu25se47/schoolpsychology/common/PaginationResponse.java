package com.fpt.gsu25se47.schoolpsychology.common;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordGetAllResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
    private List<SurveyRecordGetAllResponse> content;
}
