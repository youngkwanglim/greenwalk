package codehanzoom.greenwalk.donation.dto;

import codehanzoom.greenwalk.donation.domain.Donation;
import lombok.Getter;

@Getter
public class DonationResponse {

    private Long id;
    private String userName;
    private String partnerName;
    private int donationAmount;

    public DonationResponse(Donation donation) {
        this.id = donation.getId();
        this.userName = donation.getUser().getName();
        this.partnerName = donation.getPartner().getName();
        this.donationAmount = donation.getDonationAmount();
    }
}
