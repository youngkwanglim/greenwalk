package codehanzoom.greenwalk.user.controller;


import codehanzoom.greenwalk.global.dto.ResponseDto;
import codehanzoom.greenwalk.global.dto.UserJoinDto;
import codehanzoom.greenwalk.login.jwt.JwtService;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.repository.UserRepository;
import codehanzoom.greenwalk.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping({"","/"})
    public String index()  {
        return "<h1>index</h1>";
    }

    // ROLE_USER의 회원가입 경로
    @PostMapping("/auth/join")
    public ResponseDto<String> join(@RequestBody UserJoinDto userJoinDto) throws Exception {
        userService.join(userJoinDto);
        return new ResponseDto<String>(HttpStatus.OK.value(), "회원가입 완료");
    }
}
