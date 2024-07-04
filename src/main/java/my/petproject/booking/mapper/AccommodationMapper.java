package my.petproject.booking.mapper;

import java.util.List;
import java.util.stream.Collectors;
import my.petproject.booking.config.MapperConfig;
import my.petproject.booking.dto.accommodation.AccommodationRequestDto;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import my.petproject.booking.model.Accommodation;
import my.petproject.booking.model.Amenity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    @Mapping(target = "amenitiesIds", source = "amenities", qualifiedByName = "mapAmenitiesToIds")
    @Mapping(target = "type", source = "type", qualifiedByName = "getTypeFromEnum")
    AccommodationResponseDto toResponseDto(Accommodation accommodation);

    @Mapping(target = "amenities", source = "amenitiesIds", qualifiedByName = "mapIdsToAmenities")
    Accommodation toEntity(AccommodationRequestDto accommodationRequestDto);

    @Named("getTypeFromEnum")
    default String getTypeFromEnum(Accommodation.Type type) {
        return type.toString();
    }

    @Named("mapIdsToAmenities")
    default List<Amenity> mapIdsToAmenities(List<Long> amenitiesIds) {
        return amenitiesIds.stream().map(id -> {
            Amenity amenity = new Amenity();
            amenity.setId(id);
            return amenity;
        }).collect(Collectors.toList());
    }

    @Named("mapAmenitiesToIds")
    default List<Long> mapAmenitiesToIds(List<Amenity> amenities) {
        return amenities.stream().map(Amenity::getId).collect(Collectors.toList());
    }
}
