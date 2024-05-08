package codehanzoom.greenwalk.partner.controller;

import codehanzoom.greenwalk.partner.domain.Partner;
import codehanzoom.greenwalk.partner.dto.PartnerDto;
import codehanzoom.greenwalk.partner.dto.PartnerRequest;
import codehanzoom.greenwalk.partner.service.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "partners", description = "기부처 API")
public class PartnerController {

    private final PartnerService partnerService;

    @Operation(summary = "기부처 명단 가져오기")
    @GetMapping("/partners")
    public ResponseEntity<List<PartnerDto>> partner() {
        List<Partner> partners = partnerService.findPartners();
        List<PartnerDto> result = partners.stream()
                .map(p -> new PartnerDto(p))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "기부처 생성하기")
    @PostMapping("/partners/new")
    public ResponseEntity<String> createPartner(@RequestBody PartnerRequest partnerRequest) {
        Partner partner = PartnerRequest.toEntity(partnerRequest);
        partnerService.savePartner(partner);
        return ResponseEntity.ok("기부처 생성이 완료되었습니다.");
    }

    @Operation(summary = "기부처 수정하기")
    @PatchMapping("/partners/{id}")
    public ResponseEntity<PartnerDto> updatePartner(@PathVariable Long id, @RequestBody PartnerRequest partnerRequest) {
        Partner updatedPartner = partnerService.updatePartner(id, partnerRequest);
        PartnerDto partnerDto = new PartnerDto(updatedPartner);
        return ResponseEntity.ok(partnerDto);
    }

    @Operation(summary = "기부처 삭제하기")
    @DeleteMapping("/partners/{id}")
    public ResponseEntity<String> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.ok("기부처가 성공적으로 삭제되었습니다.");
    }
}
