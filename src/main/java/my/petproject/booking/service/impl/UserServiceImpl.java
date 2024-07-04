package my.petproject.booking.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.user.UpdateProfileRequestDto;
import my.petproject.booking.dto.user.UpdateRoleRequestDto;
import my.petproject.booking.dto.user.UserProfileResponseDto;
import my.petproject.booking.dto.user.UserRequestRegistrationDto;
import my.petproject.booking.dto.user.UserResponseDto;
import my.petproject.booking.exception.EntityNotFoundException;
import my.petproject.booking.exception.RegistrationException;
import my.petproject.booking.mapper.UserMapper;
import my.petproject.booking.model.Role;
import my.petproject.booking.model.User;
import my.petproject.booking.repository.RoleRepository;
import my.petproject.booking.repository.UserRepository;
import my.petproject.booking.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Long USER_ROLE_ID = 2L;
    private static final String CANT_FIND_BY_ID = "User with id %s is not found";
    private static final String EMAIL_RESERVED = "User with email %s already exists";
    private static final String ROLE_NOT_FOUND = "Role with name %s isn't found";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRequestRegistrationDto userRequestRegistrationDto) {
        if (userRepository.findUserByEmail(userRequestRegistrationDto.getEmail()).isPresent()) {
            throw new RegistrationException(String.format(EMAIL_RESERVED,
                    userRequestRegistrationDto.getEmail()));
        }
        User user = userMapper.toEntity(userRequestRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRequestRegistrationDto.getPassword()));
        Role role = new Role().setId(USER_ROLE_ID);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoleSet(roles);
        user.setRole(User.RoleName.USER);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserProfileResponseDto updateRole(Long id, UpdateRoleRequestDto roleRequestDto) {
        User userFromDb = getUserFromDb(id);
        Role role = roleRepository.findRoleByRoleName(roleRequestDto.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        ROLE_NOT_FOUND, roleRequestDto.getRoleName())));
        Set<Role> roleSet = userFromDb.getRoleSet();
        roleSet.clear();
        roleSet.add(role);
        userFromDb.setRoleSet(roleSet);
        if (role.getRoleName().equals(Role.RoleName.ADMIN)) {
            userFromDb.setRole(User.RoleName.ADMIN);
        } else {
            userFromDb.setRole(User.RoleName.USER);
        }
        userRepository.save(userFromDb);
        return userMapper.toResponseDtoWithRoles(userFromDb);
    }

    @Override
    public UserProfileResponseDto getProfile(Long id) {
        return userMapper.toResponseDtoWithRoles(getUserFromDb(id));
    }

    @Override
    public UserProfileResponseDto updateProfileInfo(
            Long id, UpdateProfileRequestDto profileRequestDto) {
        User userFromDb = getUserFromDb(id);
        userFromDb.setEmail(profileRequestDto.getEmail());
        userFromDb.setFirstName(profileRequestDto.getFirstName());
        userFromDb.setLastName(profileRequestDto.getLastName());
        userRepository.save(userFromDb);
        return userMapper.toResponseDtoWithRoles(userFromDb);
    }

    private User getUserFromDb(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format(CANT_FIND_BY_ID, id)));
    }
}
