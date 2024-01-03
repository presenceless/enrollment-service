package cd.presenceless.enrollmentservice.repository;

import cd.presenceless.enrollmentservice.entity.Photograph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotographRepository
        extends JpaRepository<Photograph, Long> { }
