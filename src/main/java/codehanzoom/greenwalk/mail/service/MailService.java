package codehanzoom.greenwalk.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    //메일 관련 클래스 의존성 주입
    private final JavaMailSender javaMailSender;

    //==SimpleMailMessage 생성 메소드==//
    public SimpleMailMessage createMessage(String email, String subject, String text) {

        //간단한 텍스트 전송 메일 전용 클래스
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        log.info(email);

        //송신자,제목,텍스트
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        return simpleMailMessage;
    }

    //==이메일로 메세지 전송 메소드==//
    public void sendMessageToEmail(SimpleMailMessage simpleMailMessage){
        javaMailSender.send(simpleMailMessage);
    }

}
