package my.petproject.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.booking.BookingRequestDto;
import my.petproject.booking.dto.booking.BookingResponseDto;
import my.petproject.booking.dto.booking.BookingUpdateDto;
import my.petproject.booking.model.User;
import my.petproject.booking.service.BookingService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Tag(name = "Booking management", description = "Endpoints for booking management")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new bookings",
            description = "Endpoint for creating a new bookings")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingResponseDto createNewBooking(@RequestBody @Valid BookingRequestDto requestDto,
                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return bookingService.create(requestDto, user);
    }

    @GetMapping(params = {"user_id", "status"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings by user ID and status",
            description = "Endpoint for getting all bookings by user ID and status")
    public List<BookingResponseDto> getAllByUserIdAndStatus(
            @RequestParam("user_id") Long userId,
            @RequestParam("status") String status) {
        return bookingService.getBookingByUserIdAndStatus(userId, status);
    }

    @GetMapping("/my")
    @Operation(summary = "Get booking details by id",
            description = "Endpoint for getting all booking details by id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BookingResponseDto> findAllBookingByUserId(
            Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return bookingService.getAllByUserId(user.getId(), pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking details by id",
            description = "Endpoint for getting all booking details by id")
    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponseDto findBookingById(@PathVariable Long id) {
        return bookingService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a booking by id",
            description = "Endpoint for updating a booking by id")
    @PreAuthorize("hasRole('ADMIN')")
    public BookingResponseDto updateBookingById(@PathVariable Long id,
                                                @RequestBody BookingUpdateDto requestDto) {
        return bookingService.updateBookingById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a booking by id",
            description = "Endpoint for deleting a booking by id")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBookingById(@PathVariable Long id) {
        bookingService.deleteBookingById(id);
    }
}
