package com.fpt.gsu25se47.schoolpsychology.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    TOKEN_ACTIVE("active"),
    TOKEN_EXPIRED("expired"),
    ACCOUNT_ACTIVE("active"),
    ACCOUNT_BLOCK("blocked"),
    ACCOUNT_DELETE("deleted");
    private final String value;
}