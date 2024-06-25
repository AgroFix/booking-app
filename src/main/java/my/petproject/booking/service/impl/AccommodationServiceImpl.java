package my.petproject.booking.service.impl;

import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.accommodation.AccommodationRequestDto;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import my.petproject.booking.exception.EntityNotFoundException;
import my.petproject.booking.mapper.AccommodationMapper;
import my.petproject.booking.model.Accommodation;
import my.petproject.booking.repository.AccommodationRepository;
import my.petproject.booking.service.AccommodationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    public static final String CANT_FIND_BY_ID = "Can't find accommodation with id: ";
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    @Override
    public AccommodationResponseDto createAccommodation(AccommodationRequestDto accommodationRequestDto) {
        return accommodationMapper.toResponseDto(accommodationRepository
                .save(accommodationMapper.toEntity(accommodationRequestDto)));
    }

    @Override
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccommodationResponseDto updateAccommodationById(AccommodationRequestDto accommodationRequestDto, Long id) {
        checkIfAccommodationExists(id);
        Accommodation accommodation = accommodationMapper.toEntity(accommodationRequestDto).setId(id);
        return accommodationMapper.toResponseDto(accommodationRepository.save(accommodation));
    }

    @Override
    public AccommodationResponseDto getAccommodationById(Long id) {
        Accommodation accommodation = checkIfAccommodationExists(id);
        return accommodationMapper
                .toResponseDto(accommodation);
    }

    @Override
    public void deleteAccommodationById(Long id) {
        accommodationRepository.deleteById(id);
    }

    private Accommodation checkIfAccommodationExists(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CANT_FIND_BY_ID + id));
        return accommodation;
    }
}
