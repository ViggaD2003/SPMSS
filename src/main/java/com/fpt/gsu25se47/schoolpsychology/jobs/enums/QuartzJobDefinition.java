package com.fpt.gsu25se47.schoolpsychology.jobs.enums;

import com.fpt.gsu25se47.schoolpsychology.jobs.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;

@Getter
@RequiredArgsConstructor
public enum QuartzJobDefinition {
    SLOT_STATUS(
            "updateSlotStatusJob",
            "slotJobs",
            "updateSlotStatusTrigger",
            UpdateSlotsStatusJob.class
    ),
    APPOINTMENT_STATUS(
            "updateAppoinmentStatusJob",
            "appointmentJobs",
            "updateAppointmentStatusTrigger",
            UpdateAppointmentsStatusJob.class
    ),
    CLASS_STATUS(
            "updateClassStatusJob",
            "classJobs",
            "updateClassStatusTrigger",
            UpdateClassesStatusJob.class
    ),
    SURVEY_STATUS(
            "updateSurveyStatusJob",
            "surveyJobs",
            "updateSurveyStatusTrigger",
            UpdateSurveysStatusJob.class
    ),
    RECURRING_SURVEY_STATUS(
            "processRecurringSurveyStatusJob",
            "surveyJobs",
            "processRecurringSurveyStatusTrigger",
            ProcessRecurringSurveysJob.class
    ),
    ATTENDENCE_MEETING(
            "attendenceMeetingJob",
            "meetingJobs",
            "attendenceMeetingTrigger",
            AttendenceMeetingJob.class
    ),
    PARTICIPANT_STATUS(
            "participantStatusJob",
            "participantJobs",
            "participantStatusTrigger",
            ParticipantStatusJob.class
    );

    private final String jobName;
    private final String groupName;
    private final String triggerName;
    private final Class<? extends Job> jobClass;
}
