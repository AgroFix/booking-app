package my.petproject.booking.service;

import java.util.List;
import my.petproject.booking.dto.booking.BookingRequestDto;
import my.petproject.booking.dto.booking.BookingResponseDto;
import my.petproject.booking.dto.booking.BookingUpdateDto;
import my.petproject.booking.model.User;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto bookingRequestDto, User user);

    List<BookingResponseDto> getBookingByUserIdAndStatus(Long userId, String status);

    List<BookingResponseDto> getAllByUserId(Long userId, Pageable pageable);

    BookingResponseDto getById(Long bookingId);

    BookingResponseDto updateBookingById(Long bookingId, BookingUpdateDto bookingUpdateDto);

    void deleteBookingById(Long bookingId);
}
