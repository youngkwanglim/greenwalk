package codehanzoom.greenwalk.donation.repository;

import codehanzoom.greenwalk.donation.domain.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByUserId(Long userId);

}
