package com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.CaseSummaryResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CaseSummaryMapper {

    @Mapping(target = "categoryName", source = "currentLevel.category.name")
    @Mapping(target = "categoryCode", source = "currentLevel.category.code")
    CaseSummaryResponse toCaseSummaryResponse(Cases cases, @Context Integer count);

    @AfterMapping
    default void setCountToCaseSummaryResponse(@MappingTarget CaseSummaryResponse caseSummaryResponse,
                                               @Context Integer count) {

        caseSummaryResponse.setCount(count);
    }
}
