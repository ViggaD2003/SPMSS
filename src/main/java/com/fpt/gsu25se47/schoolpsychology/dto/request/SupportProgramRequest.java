package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.utils.TodayOrFuture;
import jakarta.validation.constraints.Future;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
@Data
public class SupportProgramRequest {

    @Length(min = 1, max = 255, message = "Tên chương trình không được để trống và tối đa 255 ký tự")
    private String name;

    @Length(min = 1, max = 5000, message = "Mô tả phải từ 1 đến 5000 ký tự")
    private String description;

    @Range(min = 1, max = 200, message = "Số người tham gia phải từ 1 đến 200")
    private Integer maxParticipants;

    @TodayOrFuture
    private LocalDateTime startTime;

    @TodayOrFuture
    private LocalDateTime endTime;

    private String location;

    private Integer hostedBy;

    private AddNewSurveyDto addNewSurveyDto;
}
