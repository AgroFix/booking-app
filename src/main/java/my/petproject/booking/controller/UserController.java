package my.petproject.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.user.UpdateProfileRequestDto;
import my.petproject.booking.dto.user.UpdateRoleRequestDto;
import my.petproject.booking.dto.user.UserProfileResponseDto;
import my.petproject.booking.model.User;
import my.petproject.booking.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Managing authentication and user registration",
        description = "Managing authentication and user registration")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a user role",
            description = "Endpoint for updating a user role by user id")
    UserProfileResponseDto updateUserRole(@PathVariable Long id,
                                          @RequestBody @Valid UpdateRoleRequestDto roleRequestDto
    ) {
        return userService.updateRole(id, roleRequestDto);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get user profile info",
            description = "Endpoint for getting user profile info")
    UserProfileResponseDto getProfileInfo(Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        return userService.getProfile(user.getId());
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Update user profile info",
            description = "Endpoint for updating user profile info")
    UserProfileResponseDto updateProfileInfo(
            Authentication authentication,
            @RequestBody @Valid UpdateProfileRequestDto profileRequestDto) {
        User user = (User)authentication.getPrincipal();
        return userService.updateProfileInfo(user.getId(), profileRequestDto);
    }
}
