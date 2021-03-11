package com.study.account;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.standard.expression.Each;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public Account join(SignUpForm signUpForm) {
        Account account = Account.builder()
                .nickname(signUpForm.getNickname())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .joinedAt(LocalDateTime.now())
                .build();
        return accountRepository.save(account);
    }

    // 가입확인 이메일 발송
    public void emailSend(Account account) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(account.getEmail());
        message.setSubject("가입을 축하합니다.인증해주세요");
        message.setText("/check-token-email?token="+account.getEmailCheckToken()+"&email="+account.getEmail());
        //javaMailSender.send(message);
    }

    // 토큰생성
    public void generatedToken(Account account) {
        account.generatedToken();
    }

    // 자동로그인
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AccountUser(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(token);

    }

    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        //select * from account where email = ?
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }
        if(account == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않음");
        }

        return new AccountUser(account);
    }

    @Transactional
    public void completeSignUp(Account account) {
        generatedToken(account);
        emailSend(account);
        login(account);
    }
}
