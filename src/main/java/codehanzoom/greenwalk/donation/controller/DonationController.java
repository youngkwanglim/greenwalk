package codehanzoom.greenwalk.donation.controller;

import codehanzoom.greenwalk.partner.domain.Partner;
import codehanzoom.greenwalk.partner.dto.PartnerDto;
import codehanzoom.greenwalk.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class DonationController {

    private final PartnerService partnerService;

}
