package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class UpcomingAppointments {

    private LocalDateTime date;

    private String student;

}
