package codehanzoom.greenwalk.partner.controller;

import codehanzoom.greenwalk.partner.domain.Partner;
import codehanzoom.greenwalk.partner.dto.PartnerDto;
import codehanzoom.greenwalk.partner.service.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "partners", description = "기부처 API")
public class PartnerController {

    private final PartnerService partnerService;

    @Operation(summary = "기부처 명단 가져오기")
    @GetMapping("/partners")
    public List<PartnerDto> partner() {
        List<Partner> partners = partnerService.findPartners();
        List<PartnerDto> result = partners.stream()
                .map(p -> new PartnerDto(p))
                .collect(Collectors.toList());

        return result;
    }

//    @GetMapping("/partners/{id}"))
//    public void detailPartner(@RequestParam("sponsorId") Long sponsorId) {
//
//    }

}
