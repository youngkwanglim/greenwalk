package codehanzoom.greenwalk.donation.service;

import codehanzoom.greenwalk.donation.domain.Donation;
import codehanzoom.greenwalk.donation.repository.DonationRepository;
import codehanzoom.greenwalk.partner.domain.Partner;
import codehanzoom.greenwalk.partner.repository.PartnerRepository;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.repository.UserRepository;
import codehanzoom.greenwalk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DonationService {

    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;
    private final DonationRepository donationRepository;
    private final UserService userService;

    public List<Donation> findDonations() {
        return donationRepository.findAll();
    }

    public Donation findOne(Long donationId) {
        return donationRepository.findById(donationId).get();
    }

//    @Transactional
//    public void saveDonation (Donation donation) {
//        donationRepository.save(donation);
//    }

    @Transactional
    public void donate(Long partnerId, int donationAmount) {

        // 회원 id 반환
        Long userId= userService.getUserId();

        // 엔티티 조회
        User user = userRepository.findById(userId).get();
        Partner partner = partnerRepository.findById(partnerId).get();

        // 기부 생성
        Donation donation = Donation.createDonation(user, partner, donationAmount);

        // 기부 저장
        donationRepository.save(donation);
    }

}
