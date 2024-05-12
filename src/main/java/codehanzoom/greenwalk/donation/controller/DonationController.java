package codehanzoom.greenwalk.donation.controller;

import codehanzoom.greenwalk.donation.dto.DonationRequest;
import codehanzoom.greenwalk.donation.dto.DonationResponse;
import codehanzoom.greenwalk.donation.service.DonationService;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.dto.UserDto;
import codehanzoom.greenwalk.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "donations", description = "기부 API")
public class DonationController {

    private final DonationService donationService;
    private final UserService userService;

    @Operation(summary = "기부하기")
    @PostMapping("/donations")
    public ResponseEntity<?> donation(@RequestBody @Valid DonationRequest request) {

        donationService.donate(request.getPartnerId(), request.getDonationMoney());
        return ResponseEntity.ok("기부에 성공하였습니다.");

    }

    @Operation(summary = "기부 목록 가져오기")
    @GetMapping("/users/donations")
    public ResponseEntity<List<DonationResponse>> donationList(){
        Long userId = userService.getUserId();
        List<DonationResponse> donations = donationService.findDonationsByUserId(userId);
        if (donations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(donations);
    }
}
