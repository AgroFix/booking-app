package my.petproject.booking.repository;

import java.util.List;
import java.util.Optional;
import my.petproject.booking.model.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserId(Long userId, Pageable pageable);

    List<Booking> findAllByAccommodationIdAndStatusNot(
            Long accommodationId, Booking.Status status);

    Optional<Booking> findBookingById(Long id);

    List<Booking> findAllByStatusAndUserId(Booking.Status status,
                                           Long userId);

}
