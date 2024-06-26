package my.petproject.booking.security;

import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.user.UserRequestLoginDto;
import my.petproject.booking.dto.user.UserResponseLoginDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserResponseLoginDto authenticate(UserRequestLoginDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserResponseLoginDto(token);
    }
}
