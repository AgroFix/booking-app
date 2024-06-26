package my.petproject.booking.service;

import my.petproject.booking.dto.user.UpdateProfileRequestDto;
import my.petproject.booking.dto.user.UpdateRoleRequestDto;
import my.petproject.booking.dto.user.UserProfileResponseDto;
import my.petproject.booking.dto.user.UserRequestRegistrationDto;
import my.petproject.booking.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRequestRegistrationDto userRequestRegistrationDto);

    UserProfileResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto);

    UserProfileResponseDto getProfile(Long id);

    UserProfileResponseDto updateProfileInfo(Long id, UpdateProfileRequestDto profileRequestDto);
}
