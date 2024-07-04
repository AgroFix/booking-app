package my.petproject.booking.mapper;

import java.util.List;
import my.petproject.booking.config.MapperConfig;
import my.petproject.booking.dto.booking.BookingRequestDto;
import my.petproject.booking.dto.booking.BookingResponseDto;
import my.petproject.booking.model.Accommodation;
import my.petproject.booking.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "accommodationId", source = "accommodation.id")
    @Mapping(target = "status", source = "status", qualifiedByName = "getStatusFromEnum")
    BookingResponseDto toResponseDto(Booking booking);

    @Mapping(target = "accommodation", source = "accommodationId",
            qualifiedByName = "mapAccommodationIdToAccommodation")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStringToStatus")
    Booking toEntity(BookingRequestDto bookingRequestDto);

    List<BookingResponseDto> toResponseDtoList(List<Booking> bookings);

    @Named("getStatusFromEnum")
    default String getStatusFromEnum(Booking.Status status) {
        return status.toString();
    }

    @Named("mapStringToStatus")
    default Booking.Status mapStringToStatus(String status) {
        return Booking.Status.valueOf(status.toUpperCase());
    }

    @Named("mapAccommodationIdToAccommodation")
    default Accommodation mapAccommodationIdToAccommodation(Long accommodationId) {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationId);
        return accommodation;
    }
}
