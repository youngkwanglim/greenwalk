package codehanzoom.greenwalk.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {

    public Long id;

    public String name;

    public String email;

    public int totalPoint;

    public int totalDonation;

    public int totalStep;

    private int totalTrashCount;

    private float totalWalkingDistance;

}
