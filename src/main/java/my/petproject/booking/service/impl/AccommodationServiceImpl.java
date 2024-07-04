package my.petproject.booking.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.accommodation.AccommodationRequestDto;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import my.petproject.booking.exception.EntityNotFoundException;
import my.petproject.booking.mapper.AccommodationMapper;
import my.petproject.booking.model.Accommodation;
import my.petproject.booking.model.Amenity;
import my.petproject.booking.repository.AccommodationRepository;
import my.petproject.booking.repository.AmenityRepository;
import my.petproject.booking.service.AccommodationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    public static final String CANT_FIND_ACCOMMODATION_BY_ID
            = "Can't find accommodation with id: ";
    public static final String CANT_FIND_AMENITY_BY_ID
            = "Can't find amenity with id: ";
    private final AccommodationRepository accommodationRepository;
    private final AmenityRepository amenityRepository;
    private final AccommodationMapper accommodationMapper;

    @Transactional
    @Override
    public AccommodationResponseDto createAccommodation(
            AccommodationRequestDto accommodationRequestDto) {
        Accommodation accommodation = accommodationMapper.toEntity(accommodationRequestDto);
        setAmenitiesFromDb(accommodation, accommodationRequestDto.getAmenitiesIds());
        return accommodationMapper.toResponseDto(accommodationRepository.save(accommodation));
    }

    @Override
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AccommodationResponseDto updateAccommodationById(
            AccommodationRequestDto accommodationRequestDto, Long id) {
        getAccommodationFromDb(id);
        Accommodation accommodation = accommodationMapper
                .toEntity(accommodationRequestDto).setId(id);
        setAmenitiesFromDb(accommodation, accommodationRequestDto.getAmenitiesIds());
        return accommodationMapper.toResponseDto(accommodationRepository.save(accommodation));
    }

    @Transactional
    @Override
    public AccommodationResponseDto getAccommodationById(Long id) {
        Accommodation accommodation = getAccommodationFromDb(id);
        return accommodationMapper
                .toResponseDto(accommodation);
    }

    @Override
    public void deleteAccommodationById(Long id) {
        Accommodation accommodation = getAccommodationFromDb(id);
        accommodationRepository.deleteById(accommodation.getId());
    }

    private Accommodation getAccommodationFromDb(Long id) {
        return accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CANT_FIND_ACCOMMODATION_BY_ID + id));
    }

    private Amenity getAmenityFromDb(Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CANT_FIND_AMENITY_BY_ID + id));
    }

    private void setAmenitiesFromDb(Accommodation accommodation, List<Long> amenitiesIds) {
        List<Amenity> amenities = amenitiesIds.stream()
                .map(this::getAmenityFromDb)
                .collect(Collectors.toList());
        accommodation.setAmenities(amenities);
    }
}
