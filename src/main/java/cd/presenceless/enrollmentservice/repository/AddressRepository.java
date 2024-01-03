package cd.presenceless.enrollmentservice.repository;

import cd.presenceless.enrollmentservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository
        extends JpaRepository<Address, Long> { }
