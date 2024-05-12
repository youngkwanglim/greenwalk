package codehanzoom.greenwalk.mail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestEmailDto {
    private String email;
    private String authNumber;
}
