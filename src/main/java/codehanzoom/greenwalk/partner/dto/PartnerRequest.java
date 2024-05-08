package codehanzoom.greenwalk.partner.dto;

import codehanzoom.greenwalk.partner.domain.Partner;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PartnerRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String introduction;

    public static Partner toEntity(PartnerRequest partnerRequest) {
        return Partner.builder()
                .name(partnerRequest.getName())
                .introduction(partnerRequest.getIntroduction())
                .build();
    }
}
