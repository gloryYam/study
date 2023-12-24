package io.lmy.app.modules.account.controller;

import io.lmy.app.modules.account.controller.validator.SignUpFormValidator;
import io.lmy.app.modules.account.domain.entity.Account;
import io.lmy.app.modules.account.dto.SignUpFormDto;
import io.lmy.app.modules.account.infra.repository.AccountRepository;
import io.lmy.app.modules.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpFormDto());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpFormDto signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        Account account = accountService.signUp(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String verifyEmail(String token, String email, Model model) {

        // 이메일에 해당하는 계정을 찾습니다
        Account account = accountService.findAccountByEmail(email);

        // 계정이 존재하지 않는 경우
        if (account == null) {  // 이메일이 null 이면
            model.addAttribute("error", "wrong.email");
            return "account/email-verification";
        }

        // 토큰 값과 계정의 이메일 토큰 값을 비교합니다.
        if (!token.equals(account.getEmailToken())) {   // 토큰 값이 일치하지 않은 경우
            model.addAttribute("error", "wrong.token");
            return "account/email-verification";
        }

        // 토큰 값이 일치하는 경우
        account.verified();
        accountService.login(account);
        // "numberOfUsers" 속성에 계정 레포지토리의 총 계정 수를 추가합니다.
        model.addAttribute("numberOfUsers", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());

        return "account/email-verification";
    }
}