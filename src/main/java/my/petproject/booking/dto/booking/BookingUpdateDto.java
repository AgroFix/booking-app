package my.petproject.booking.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingUpdateDto {
    @NotNull(message = " cannot be null or empty")
    @FutureOrPresent
    private LocalDate checkInDate;

    @NotNull(message = " cannot be null or empty")
    @Future
    private LocalDate checkOutDate;

    @NotBlank
    private String status;
}
