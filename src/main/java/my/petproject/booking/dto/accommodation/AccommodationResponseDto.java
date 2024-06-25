package my.petproject.booking.dto.accommodation;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccommodationResponseDto {

    private Long id;

    private String type;

    private String location;

    private String size;

    private List<String> amenities;

    private BigDecimal rate;

    private Integer availability;
}
