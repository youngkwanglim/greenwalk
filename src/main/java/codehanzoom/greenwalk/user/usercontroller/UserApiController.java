package codehanzoom.greenwalk.user.usercontroller;


import codehanzoom.greenwalk.global.dto.ResponseDto;
import codehanzoom.greenwalk.global.dto.UserJoinDto;
import codehanzoom.greenwalk.user.userrepository.UserRepository;
import codehanzoom.greenwalk.user.userservice.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping({"","/"})
    public String index()  {
        return "index";
    }

    // ROLE_USER의 회원가입 경로
    @PostMapping("/auth/join")
    public ResponseDto<String> join(@RequestBody UserJoinDto userJoinDto) throws Exception {
        userService.join(userJoinDto);
        return new ResponseDto<String>(HttpStatus.OK.value(), "회원가입 완료");
    }

    // 이메일 중복 확인 경로
    @GetMapping("/auth/join/{email}/exists_email")
    public ResponseDto checkEmailDuplicate(@PathVariable String email){
        log.info("컨트롤러 진입 email 인증");
        return new ResponseDto(HttpStatus.OK.value(), userService.checkEmailDuplicate(email));
    }

    // nickname 중복 확인 경로
    @GetMapping("/auth/join/{nickname}/exists_nickname")
    public ResponseDto checkNicknameDuplicate(@PathVariable String nickname){
        log.info("컨트롤러 진입 nickname 인증");
        return new ResponseDto(HttpStatus.OK.value(),userService.checkNicknameDuplicate(nickname));
    }

    // jwt Token 전송 여부 확인 경로(임시)
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
}
