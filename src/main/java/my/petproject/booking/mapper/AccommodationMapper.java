package my.petproject.booking.mapper;

import my.petproject.booking.config.MapperConfig;
import my.petproject.booking.dto.accommodation.AccommodationRequestDto;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import my.petproject.booking.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    @Mapping(target = "type", source = "type", qualifiedByName = "getTypeFromEnum")
    AccommodationResponseDto toResponseDto(Accommodation accommodation);

    Accommodation toEntity(AccommodationRequestDto accommodationRequestDto);

    @Named("getTypeFromEnum")
    default String getTypeFromEnum(Accommodation.Type type) {
        return type.toString();
    }
}
