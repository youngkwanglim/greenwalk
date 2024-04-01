package codehanzoom.greenwalk.partner.repository;

import codehanzoom.greenwalk.partner.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}
