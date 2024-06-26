package my.petproject.booking.security;

import lombok.RequiredArgsConstructor;
import my.petproject.booking.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private static final String USER_NOT_FOUND_BY_EMAIL_MSG = "Can't find user by email: %s";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException(String
                .format(USER_NOT_FOUND_BY_EMAIL_MSG, email)));
    }
}
