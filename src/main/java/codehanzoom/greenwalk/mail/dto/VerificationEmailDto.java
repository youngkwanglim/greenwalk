package codehanzoom.greenwalk.mail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationEmailDto {
    private String email;
    private String authNumber;
}

