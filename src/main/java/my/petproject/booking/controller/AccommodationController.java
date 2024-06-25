package my.petproject.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.accommodation.AccommodationRequestDto;
import my.petproject.booking.dto.accommodation.AccommodationResponseDto;
import my.petproject.booking.service.AccommodationService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
@Tag(name = "Accommodation management", description = "Endpoints for accommodation management")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new accommodation",
            description = "Endpoint for creating a new accommodation")
    public AccommodationResponseDto create(
            @RequestBody @Valid AccommodationRequestDto accommodationRequestDto) {
        return accommodationService.createAccommodation(accommodationRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all accommodations",
            description = "Endpoint for getting a list of all accommodations")
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get accommodations by id",
            description = "Endpoint for getting accommodation by input id")
    public AccommodationResponseDto getAccommodationById(@PathVariable Long id) {
        return accommodationService.getAccommodationById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a accommodation",
            description = "Endpoint for updating data about the existing accommodation by ID")
    public AccommodationResponseDto update(
            @RequestBody @Valid AccommodationRequestDto accommodationRequestDto,
            @PathVariable Long id
    ) {
        return accommodationService.updateAccommodationById(accommodationRequestDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a accommodation",
            description = "Endpoint for marking existing accommodation for deletion by ID")
    public void delete(@PathVariable Long id) {
        accommodationService.deleteAccommodationById(id);
    }
}
