package codehanzoom.greenwalk.partner.service;

import codehanzoom.greenwalk.partner.domain.Partner;
import codehanzoom.greenwalk.partner.dto.PartnerRequest;
import codehanzoom.greenwalk.partner.repository.PartnerRepository;
import codehanzoom.greenwalk.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;

    public List<Partner> findPartners() {
        return partnerRepository.findAll();
    }

    @Transactional
    public void savePartner(Partner partner) {
        partnerRepository.save(partner);
    }

    @Transactional
    public Partner updatePartner(Long id, PartnerRequest partnerRequest) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("파트너를 찾을 수 없습니다."));

        if (partnerRequest.getName() != null) {
            partner.setName(partnerRequest.getName());
        }
        if (partnerRequest.getIntroduction() != null) {
            partner.setIntroduction(partnerRequest.getIntroduction());
        }
        return partner;
    }

    @Transactional
    public void deletePartner(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("파트너를 찾을 수 없습니다."));
        partnerRepository.delete(partner);
    }

    @Transactional
    public void updateDonationAmount(Long partnerId, int donateAmount) {
        Partner findPartner = partnerRepository.findById(partnerId).get();
        findPartner.addDonationAmount(donateAmount);
    }
}
