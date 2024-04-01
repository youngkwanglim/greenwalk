package codehanzoom.greenwalk.mail.service;

import codehanzoom.greenwalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MailAuthService {

    private final MailService mailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    //==이메일에 인증코드 전송 메소드==//
    public void sendCodeToEmail(String email) {

        String subject = " [GreenWalk] 이메일 인증 번호 ";
        String AuthCode = this.createCode();

        //email,제목,인증번호를 토대로 메일 작성 후 전송
        SimpleMailMessage simpleMailMessage = mailService.createMessage(email, subject, AuthCode);
        mailService.sendMessageToEmail(simpleMailMessage);

        //Redis를 사용하기 위해 ValueOperations 객체 생성 후 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email,AuthCode);
        log.info("저장된 인증 번호 : " + valueOperations.get(email));

    }

    //==인증번호 생성 메소드==//
    public String createCode(){

        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //==인증번호 검증 메소드==//
    public boolean verifiedCode(String email, String authCode) throws IllegalArgumentException {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String findAuthCode = valueOperations.get(email);
        return findAuthCode.equals(authCode);
    }
}
