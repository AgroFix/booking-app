package my.petproject.booking.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.booking.BookingRequestDto;
import my.petproject.booking.dto.booking.BookingResponseDto;
import my.petproject.booking.dto.booking.BookingUpdateDto;
import my.petproject.booking.exception.EntityNotFoundException;
import my.petproject.booking.exception.IllegalStateException;
import my.petproject.booking.mapper.BookingMapper;
import my.petproject.booking.model.Accommodation;
import my.petproject.booking.model.Booking;
import my.petproject.booking.model.User;
import my.petproject.booking.repository.AccommodationRepository;
import my.petproject.booking.repository.BookingRepository;
import my.petproject.booking.repository.UserRepository;
import my.petproject.booking.service.BookingService;
import my.petproject.booking.service.TelegramService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final String CANT_FIND_BOOKING_BY_ID = "Can't find a booking with id: ";
    private static final String CANT_FIND_USER_BY_ID = "Can't find a user with id: ";
    private static final String CANT_FIND_ACCOMMODATION_BY_ID
            = "Can't find a accommodation with id: ";
    private static final String INVALID_DATE_EXCEPTION
            = "Accommodation is already booked for the specified date range";
    private static final String DOUBLE_CANCELED_EXCEPTION
            = "Accommodation is already canceled";
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final TelegramService telegramService;

    @Transactional
    @Override
    public BookingResponseDto create(BookingRequestDto bookingRequestDto, User user) {
        Accommodation accommodation = getAccommodationFromDb(
                bookingRequestDto.getAccommodationId());
        if (existBookingAccommodationWithSameDate(
                accommodation.getId(),
                bookingRequestDto.getCheckInDate(),
                bookingRequestDto.getCheckOutDate())
        ) {
            throw new IllegalStateException(INVALID_DATE_EXCEPTION);
        }
        Booking booking = bookingMapper.toEntity(bookingRequestDto);
        booking.setAccommodation(accommodation);
        booking.setStatus(Booking.Status.valueOf(bookingRequestDto.getStatus()));
        booking.setUser(user);
        BookingResponseDto responseDto = bookingMapper
                .toResponseDto(bookingRepository.save(booking));
        telegramService.sendBookingCreateMessage(responseDto);
        return responseDto;
    }

    @Override
    public List<BookingResponseDto> getBookingByUserIdAndStatus(Long userId, String status) {
        User userFromDb = getUserFromDb(userId);
        return bookingRepository.findAllByStatusAndUserId(
                        Booking.Status.valueOf(status), userFromDb.getId()).stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getAllByUserId(Long userId, Pageable pageable) {
        return bookingMapper.toResponseDtoList(bookingRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public BookingResponseDto getById(Long bookingId) {
        Booking booking = getBookingFromDb(bookingId);
        return bookingMapper.toResponseDto(booking);
    }

    @Transactional
    @Override
    public BookingResponseDto updateBookingById(Long bookingId,
                                                BookingUpdateDto bookingUpdateDto
    ) {
        Booking booking = getBookingFromDb(bookingId);

        if (booking.getStatus() != Booking.Status.CANCELED) {
            booking.setStatus(Booking.Status.valueOf(bookingUpdateDto.getStatus()));
        } else {
            throw new IllegalStateException(DOUBLE_CANCELED_EXCEPTION);
        }

        booking.setCheckInDate(bookingUpdateDto.getCheckInDate());
        booking.setCheckOutDate(bookingUpdateDto.getCheckOutDate());

        BookingResponseDto responseDto = bookingMapper
                .toResponseDto(bookingRepository.save(booking));
        telegramService.sendBookingUpdateMessage(responseDto);
        return responseDto;
    }

    @Override
    public void deleteBookingById(Long bookingId) {
        Booking booking = getBookingFromDb(bookingId);
        bookingRepository.deleteById(booking.getId());
    }

    private Accommodation getAccommodationFromDb(Long id) {
        return accommodationRepository.findAccommodationById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                String.format(CANT_FIND_ACCOMMODATION_BY_ID, id)));
    }

    private Booking getBookingFromDb(Long id) {
        return bookingRepository.findBookingById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format(CANT_FIND_BOOKING_BY_ID, id)));
    }

    private User getUserFromDb(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format(CANT_FIND_USER_BY_ID, id)));
    }

    public boolean existBookingAccommodationWithSameDate(
            Long accommodationId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> bookings = bookingRepository
                .findAllByAccommodationIdAndStatusNot(
                        accommodationId, Booking.Status.CANCELED);
        return bookings.stream().anyMatch(booking ->
                (checkInDate.isBefore(booking.getCheckOutDate())
                        && checkOutDate.isAfter(booking.getCheckInDate()))
                        || (checkOutDate.isBefore(booking.getCheckOutDate())
                        && checkOutDate.isAfter(booking.getCheckInDate()))
                        || (booking.getCheckInDate().isBefore(checkOutDate)
                        && booking.getCheckOutDate().isAfter(checkInDate))
        );
    }
}
