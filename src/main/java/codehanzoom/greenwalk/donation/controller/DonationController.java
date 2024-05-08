package codehanzoom.greenwalk.donation.controller;

import codehanzoom.greenwalk.donation.dto.DonationRequest;
import codehanzoom.greenwalk.donation.service.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@Tag(name = "donations", description = "기부 API")
public class DonationController {

    private final DonationService donationService;

    @Operation(summary = "기부하기")
    @PostMapping("/donations")
    public ResponseEntity<?> donation(@RequestBody @Valid DonationRequest request) {

        donationService.donate(request.getPartnerId(), request.getDonationMoney());
        return ResponseEntity.ok("기부에 성공하였습니다.");

    }
}
