package codehanzoom.greenwalk.donation.repository;

import codehanzoom.greenwalk.donation.domain.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
