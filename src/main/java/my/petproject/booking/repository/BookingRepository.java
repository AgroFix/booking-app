package my.petproject.booking.repository;

import java.util.List;
import java.util.Optional;
import my.petproject.booking.model.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserId(Long userId);

    List<Booking> findAllByUserId(Long userId, Pageable pageable);

    List<Booking> findAllByAccommodationIdAndStatusNot(
            Long accommodationId, Booking.Status status);

    Optional<Booking> findBookingById(Long id);

    @Query("SELECT b FROM Booking b "
            + "JOIN FETCH b.accommodation a "
            + "JOIN FETCH a.amenities "
            + "WHERE b.user.id = :userId")
    List<Booking> findAllBookingsWithAmenitiesByUserId(@Param("userId") Long userId);

    List<Booking> findAllByStatusAndUserId(Booking.Status status,
                                           Long userId);

}
