package codehanzoom.greenwalk.mail.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("email")
@RequiredArgsConstructor
@AllArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String email;

    String authCode;

    public Email(String toEmail, String authCode) {
        this.email = toEmail;
        this.authCode = authCode;
    }

}
