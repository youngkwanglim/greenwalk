package codehanzoom.greenwalk.mail.controller;


import codehanzoom.greenwalk.mail.dto.RequestEmailDto;
import codehanzoom.greenwalk.mail.dto.VerificationEmailDto;
import codehanzoom.greenwalk.mail.service.MailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "mails", description = "이메일 인증 API")
public class MailController {

    private final MailAuthService mailAuthService;
    private final RedisTemplate<String, String> redisTemplate;

    @Operation(summary = "이메일 인증번호 요청")
    @PostMapping("/auth/join/verificationRequests")
    public ResponseEntity<String> getEmailAuthNumber(@RequestBody RequestEmailDto requestEmailDto){
        log.info("컨트롤러 진입 이메일 인증");
        mailAuthService.sendCodeToEmail(requestEmailDto.getEmail());
        return ResponseEntity.ok("인증번호 전송 완료");
    }

    // 이메일 인증번호 확인 컨트롤러(서버에서 인증번호 전송이 된 후에 실행되는 메서드)
    @Operation(summary = "이메일 인증번호 확인")
    @PostMapping("/auth/join/Verification")
    public ResponseEntity<Boolean> matchEmailAuthNumber(@RequestBody VerificationEmailDto verificationEmailDto){

        return ResponseEntity.ok(mailAuthService.verifiedCode(verificationEmailDto.getEmail(), verificationEmailDto.getAuthNumber()));

    }

    @Operation(summary = "Redis 테스트")
    @GetMapping("/auth/redisTest")
    public String getRedisName(@RequestParam("email") String email){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email,email);
        String getEmail = valueOperations.get(email);
        return getEmail;
    }
}
