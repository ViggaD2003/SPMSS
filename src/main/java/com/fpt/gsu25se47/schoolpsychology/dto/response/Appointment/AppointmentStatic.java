package com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment;

import com.fpt.gsu25se47.schoolpsychology.dto.response.DataSet;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AppointmentStatic {

    private int activeAppointments;

    private int completedAppointments;

    private int numOfAbsent;

    private int numOfCancel;

    private List<DataSet> dataSet;
}
