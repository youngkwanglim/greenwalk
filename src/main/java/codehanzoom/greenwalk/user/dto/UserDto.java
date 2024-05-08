package codehanzoom.greenwalk.user.dto;

import codehanzoom.greenwalk.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    public Long id;
    public String name;
    public String email;
    public int totalPoint;
    public int totalDonation;
    public int totalStep;
    private int totalTrashCount;
    private double totalWalkingDistance;

}
