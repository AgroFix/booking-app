package my.petproject.booking.service;

import java.util.List;
import my.petproject.booking.dto.accommodation.AccommodationRequestDto;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationResponseDto createAccommodation(AccommodationRequestDto accommodationRequestDto);

    List<AccommodationResponseDto> getAll(Pageable pageable);

    AccommodationResponseDto updateAccommodationById(
            AccommodationRequestDto accommodationRequestDto, Long id);

    AccommodationResponseDto getAccommodationById(Long id);

    void deleteAccommodationById(Long id);
}
