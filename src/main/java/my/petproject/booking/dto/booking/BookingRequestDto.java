package my.petproject.booking.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingRequestDto {
    @NotNull(message = " cannot be null or empty")
    @FutureOrPresent
    private LocalDate checkInDate;

    @NotNull(message = " cannot be null or empty")
    @Future
    private LocalDate checkOutDate;

    @NotNull
    @Positive(message = " cannot be less then 1")
    private Long accommodationId;

    @NotBlank
    private String status;
}
