package my.petproject.booking.service;

import my.petproject.booking.dto.accommodation.AccommodationRequestDto;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccommodationService {
    AccommodationResponseDto createAccommodation(AccommodationRequestDto accommodationRequestDto);

    List<AccommodationResponseDto> getAll(Pageable pageable);

    AccommodationResponseDto updateAccommodationById(AccommodationRequestDto accommodationRequestDto, Long id);

    AccommodationResponseDto getAccommodationById(Long id);

    void deleteAccommodationById(Long id);
}
