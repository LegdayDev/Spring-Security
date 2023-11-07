package com.cos.security1.controller;


import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("===========/test/login============");
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        System.out.println("==================================");

        System.out.println("userDetails.getUsername() = " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String oauthLoginTest(@AuthenticationPrincipal OAuth2User oAuth2User){
        System.out.println("===========/test/oauth/login============");
        System.out.println("principalDetails.getUser() = " + oAuth2User.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user){
        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 권한만 접근가능
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 해당 메서드 실행 전에 실행된다.
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 정보";
    }
}
