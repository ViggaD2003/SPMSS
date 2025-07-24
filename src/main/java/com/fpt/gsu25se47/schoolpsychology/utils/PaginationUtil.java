package com.fpt.gsu25se47.schoolpsychology.utils;

import com.fpt.gsu25se47.schoolpsychology.common.PaginationResponse;
import com.fpt.gsu25se47.schoolpsychology.configuration.PaginationProperties;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordGetAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PaginationUtil {

    private final PaginationProperties paginationProperties;

    public PageRequest getPageRequest(Integer page, Integer size, String direction, String field) {

        int pageNumber = (page != null) ? page - 1 : paginationProperties.getDefaultPageNumber();
        int pageSize = (size != null) ? size : paginationProperties.getDefaultPageSize();

        return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(direction), field));
    }

    public PaginationResponse getPaginationResponse(PageRequest pageRequest, Page<?> response, List<SurveyRecordGetAllResponse> content) {
        return PaginationResponse.builder()
                .page(pageRequest.getPageNumber())
                .size(pageRequest.getPageSize())
                .totalElements(response.getTotalElements())
                .totalPages(response.getTotalPages())
                .hasPrevious(response.hasPrevious())
                .hasNext(response.hasNext())
                .content(content)
                .build();
    }
}
