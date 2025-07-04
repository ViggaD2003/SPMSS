package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponse {

    private Integer id;

    private String hostName;

    private String bookForName;

    private String bookByName;

    private HostType hostType;

    private String location;

    private String reason;

    private AppointmentStatus status;

    private Boolean isOnline;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
