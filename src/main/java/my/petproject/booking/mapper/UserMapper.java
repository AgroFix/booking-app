package my.petproject.booking.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import my.petproject.booking.config.MapperConfig;
import my.petproject.booking.dto.user.UserProfileResponseDto;
import my.petproject.booking.dto.user.UserRequestRegistrationDto;
import my.petproject.booking.dto.user.UserResponseDto;
import my.petproject.booking.model.Role;
import my.petproject.booking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequestRegistrationDto registrationDto);

    UserResponseDto toResponseDto(User user);

    @Mapping(target = "roleIds", source = "roleSet", qualifiedByName = "toSetIds")
    UserProfileResponseDto toResponseDtoWithRoles(User user);

    @Named(value = "toSetIds")
    default Set<Long> toSetIds(Set<Role> roleSet) {
        return roleSet.stream().map(Role::getId).collect(Collectors.toSet());
    }
}
