package codehanzoom.greenwalk.user.dto;

import codehanzoom.greenwalk.partner.domain.Partner;
import lombok.Getter;

@Getter
public class UserDto {

    private Long id;

    public UserDto(Long id) {
        this.id = id;
    }
}
