package my.petproject.booking.mapper;

import my.petproject.booking.config.MapperConfig;
import my.petproject.booking.dto.health.HealthResponseDto;
import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;

@Mapper(config = MapperConfig.class)
public interface HealthMapper {
    HealthResponseDto toDto(String response, HttpStatus httpStatus);
}
