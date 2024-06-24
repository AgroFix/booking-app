package my.petproject.booking.service.impl;

import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.health.HealthResponseDto;
import my.petproject.booking.mapper.HealthMapper;
import my.petproject.booking.service.HealthService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthServiceImpl implements HealthService {
    private static final String STATUS_SUCCESS = "Success!";
    private static final String STATUS_FAILED = "Failed...";
    private final HealthMapper healthMapper;

    @Override
    public HealthResponseDto checkHealth() {
        if (validateHealth()) {
            return healthMapper.toDto(STATUS_SUCCESS, HttpStatus.OK);
        } else {
            return healthMapper.toDto(STATUS_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    boolean validateHealth() {
        return true;
    }
}
