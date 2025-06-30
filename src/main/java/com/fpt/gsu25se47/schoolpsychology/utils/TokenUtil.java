package com.fpt.gsu25se47.schoolpsychology.utils;

import com.fpt.gsu25se47.schoolpsychology.common.Status;
import com.fpt.gsu25se47.schoolpsychology.model.Token;
import com.fpt.gsu25se47.schoolpsychology.repository.TokenRepository;

public class TokenUtil {

    public static void handleExpiredToken(String jwt, TokenRepository tokenRepo) {
        Token token = tokenRepo.findByValueAndStatus(jwt, Status.TOKEN_ACTIVE.getValue()).orElse(null);
        if (token != null) {
            token.setStatus(Status.TOKEN_EXPIRED.getValue());
            tokenRepo.save(token);
        }
    }
}
