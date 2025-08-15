package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;

import java.util.List;

public interface SchoolYearService {

    List<SchoolYearResponse> getSchoolYears();
}
