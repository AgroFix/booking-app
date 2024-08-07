package my.petproject.booking.repository;

import java.util.Optional;
import my.petproject.booking.model.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Query("""
            select a from Accommodation a join fetch a.amenities
            """)
    Page<Accommodation> findAll(Pageable pageable);

    @Query("""
       select a from Accommodation a 
       left join fetch a.amenities 
       where a.id = :id
            """)
    Optional<Accommodation> findAccommodationById(Long id);
}
