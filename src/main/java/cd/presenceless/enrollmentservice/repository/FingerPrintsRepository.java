package cd.presenceless.enrollmentservice.repository;

import cd.presenceless.enrollmentservice.entity.FingerPrints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FingerPrintsRepository
        extends JpaRepository<FingerPrints, Long> { }
