package cd.presenceless.enrollmentservice.repository;

import cd.presenceless.enrollmentservice.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenRepository
        extends JpaRepository<Citizen, Long> { }
