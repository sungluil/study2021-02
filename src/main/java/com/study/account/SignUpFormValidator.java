package com.study.account;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Configuration
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        if(accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email","invalid email",new Object[]{signUpForm.getEmail()},"존재하는 이메일 입니다.");
        }
        if(accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname","invalid nickname",new Object[]{signUpForm.getNickname()},"존재하는 닉네 입니다.");
        }
    }
}
