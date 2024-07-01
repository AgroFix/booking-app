package my.petproject.booking.repository;

import java.util.Optional;
import my.petproject.booking.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    Optional<Accommodation> findAccommodationById(Long id);
}
