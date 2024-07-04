package my.petproject.booking.repository;

import java.util.Optional;
import my.petproject.booking.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Optional<Amenity> findById(Long id);
}
