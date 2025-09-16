package com.fpt.gsu25se47.schoolpsychology.model.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account");


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
