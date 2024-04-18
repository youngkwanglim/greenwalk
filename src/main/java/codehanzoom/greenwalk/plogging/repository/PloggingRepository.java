package codehanzoom.greenwalk.plogging.repository;

import codehanzoom.greenwalk.plogging.domain.entity.Plogging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PloggingRepository extends JpaRepository<Plogging, Long> {
}
