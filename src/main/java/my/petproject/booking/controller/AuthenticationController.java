package my.petproject.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.user.UserRequestLoginDto;
import my.petproject.booking.dto.user.UserRequestRegistrationDto;
import my.petproject.booking.dto.user.UserResponseDto;
import my.petproject.booking.dto.user.UserResponseLoginDto;
import my.petproject.booking.security.AuthenticationService;
import my.petproject.booking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication management", description = "Endpoints for user login and registration")
@RequestMapping("/api")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user", description = "Endpoint for user registration")
    public UserResponseDto register(@Valid @RequestBody
                                        UserRequestRegistrationDto userRequestDto) {
        return userService.register(userRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login user", description = "Endpoint for user login")
    public UserResponseLoginDto login(@Valid @RequestBody
                                      UserRequestLoginDto userRequestLoginDto) {
        return authenticationService.authenticate(userRequestLoginDto);
    }
}
