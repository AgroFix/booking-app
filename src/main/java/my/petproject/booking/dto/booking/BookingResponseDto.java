package my.petproject.booking.dto.booking;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingResponseDto {
    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Long accommodationId;

    private Long userId;

    private String status;
}
