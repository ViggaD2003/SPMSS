package com.fpt.gsu25se47.schoolpsychology.configuration;


import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String email = oAuth2User.getAttribute("email");
        Optional<Account> userOptional = accountRepository.findByEmail(email);
        Account account = userOptional.get();;

        return new DefaultOAuth2User(account.getAuthorities(), oAuth2User.getAttributes(), "email");
    }
}
