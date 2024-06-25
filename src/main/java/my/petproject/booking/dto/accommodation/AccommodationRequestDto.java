package my.petproject.booking.dto.accommodation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AccommodationRequestDto {
    @NotBlank
    private String type;
    @NotBlank
    private String location;
    @NotBlank
    private String size;

    private List<String> amenities;
    @Min(0)
    @NotNull
    private BigDecimal rate;
    @Min(0)
    @NotNull
    private Integer availability;
}
