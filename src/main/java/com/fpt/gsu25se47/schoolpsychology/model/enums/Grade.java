package com.fpt.gsu25se47.schoolpsychology.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Grade {
    NONE(0, "Không xác định"),
    GRADE_10(10, "Lớp 10"),
    GRADE_11(11, "Lớp 11"),
    GRADE_12(12, "Lớp 12");

    private final int value;
    private final String label;

    public static Grade fromValue(int value) {
        for (Grade grade : Grade.values()) {
            if (grade.value == value) {
                return grade;
            }
        }
        return NONE; // hoặc throw exception tuỳ yêu cầu
    }
}
