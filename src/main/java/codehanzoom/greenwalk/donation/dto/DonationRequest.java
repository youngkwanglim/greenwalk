package codehanzoom.greenwalk.donation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
public class DonationRequest {

    @NotNull
    private Long partnerId;

    @Min(value = 1, message = "기부 금액은 1보다 커야 합니다.")
    private int donationMoney;
}
