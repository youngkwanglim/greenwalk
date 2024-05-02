package codehanzoom.greenwalk.donation.controller;

import codehanzoom.greenwalk.donation.dto.DonationRequest;
import codehanzoom.greenwalk.donation.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/donations")
    public ResponseEntity<?> donation(@RequestBody @Valid DonationRequest request) {

        donationService.donate(request.getPartnerId(), request.getDonationMoney());
        return ResponseEntity.ok("기부에 성공하였습니다.");

    }
}
