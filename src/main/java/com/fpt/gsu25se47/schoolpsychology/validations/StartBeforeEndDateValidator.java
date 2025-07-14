package com.fpt.gsu25se47.schoolpsychology.validations;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.repository.ProgramSessionRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SupportProgramRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class    StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDateConstraint, Object> {

    private final SlotRepository slotRepository;
    private final ProgramSessionRepository programSessionRepository;
    private final SupportProgramRepository supportProgramRepository;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) return true;

        return switch (object) {
            case SupportProgramRequest request -> {
                if (request.getStartDate() == null || request.getEndDate() == null) {
                    yield true;
                }

                yield request.getEndDate().isAfter(request.getStartDate());
            }
            case CreateProgramSessionRequest request -> {
                Slot slot = slotRepository.findById(request.getSlotId()).orElse(null);

                SupportProgram supportProgram = supportProgramRepository.findById(request.getSupportProgramId()).orElse(null);

                if (slot == null || supportProgram == null || slot.getStartDateTime() == null || slot.getEndDateTime() == null || supportProgram.getStartDate() == null || supportProgram.getEndDate() == null) {
                    yield true;
                }

                boolean isWithinSlot = !request.getDate().isBefore(slot.getStartDateTime().toLocalDate()) && !request.getDate().isAfter(slot.getEndDateTime().toLocalDate()) && !request.getDate().isBefore(supportProgram.getStartDate()) && !request.getDate().isAfter(supportProgram.getEndDate());

                if (!isWithinSlot) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate("Date must be within the slot time range: " + slot.getStartDateTime() + " - " + slot.getEndDateTime()).addPropertyNode("date").addConstraintViolation();
                }

                yield isWithinSlot;
            }
            default -> throw new IllegalStateException("Unexpected value: " + object);
        };
    }
}
