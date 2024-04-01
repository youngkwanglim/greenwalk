package codehanzoom.greenwalk.partner.controller;

import codehanzoom.greenwalk.partner.domain.Partner;
import codehanzoom.greenwalk.partner.dto.PartnerDto;
import codehanzoom.greenwalk.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/partners")
    public List<PartnerDto> partner() {
        List<Partner> partners = partnerService.findPartners();
        List<PartnerDto> result = partners.stream()
                .map(p -> new PartnerDto(p))
                .collect(toList());

        return result;
    }

//    @GetMapping("/partners/{id}"))
//    public void detailPartner(@RequestParam("sponsorId") Long sponsorId) {
//
//    }

}
