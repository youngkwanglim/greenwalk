package codehanzoom.greenwalk.donation.dto;

import codehanzoom.greenwalk.donation.domain.Donation;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class DonationResponse {

    private Long id;
    private String userName;
    private String partnerName;
    private int donationAmount;
    private LocalDateTime createDate;

    public DonationResponse(Donation donation) {
        this.id = donation.getId();
        this.userName = donation.getUser().getName();
        this.partnerName = donation.getPartner().getName();
        this.donationAmount = donation.getDonationAmount();
        this.createDate = donation.getCreateDate();
    }
}
