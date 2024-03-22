package codehanzoom.greenwalk.mail.emailcontroller;


import codehanzoom.greenwalk.global.dto.ResponseDto;
import codehanzoom.greenwalk.mail.emailservice.MailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailAuthService mailAuthService;
    private final RedisTemplate<String, String> redisTemplate;

    // 이메일 인증번호 요청 컨트롤러
    @GetMapping("/auth/join/Verification_requests")
    public ResponseDto<String> getEmailAuthNumber(@RequestParam("email") String email){
        log.info("컨트롤러 진입 이메일 인증");
        mailAuthService.sendCodeToEmail(email);
        return new ResponseDto<String>(HttpStatus.OK.value(),"인증번호 전송 완료");
    }

    // 이메일 인증번호 확인 컨트롤러(서버에서 인증번호 전송이 된 후에 실행되는 메서드)
    @GetMapping("/auth/join/Verification")
    public ResponseDto<Boolean> matchEmailAuthNumber(@RequestParam("email") String email, @RequestParam("AuthNumber") String AuthNumber){
        return new ResponseDto<Boolean>(HttpStatus.OK.value(),mailAuthService.verifiedCode(email,AuthNumber));
    }

    @GetMapping("/auth/redisTest")
    public String getRedisName(@RequestParam("email") String email){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email,email);
        String getEmail = valueOperations.get(email);
        return getEmail;
    }
}
