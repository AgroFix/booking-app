package my.petproject.booking.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import my.petproject.booking.model.Role;

@Data
public class UpdateRoleRequestDto {
    @NotNull(message = " can't be empty")
    private Role.RoleName roleName;
}
