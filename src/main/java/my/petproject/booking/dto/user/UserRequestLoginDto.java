package my.petproject.booking.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequestLoginDto {
    @Email(message = " is incorrect")
    private String email;
    @Size(min = 8, message = " must be longer")
    @Size(max = 40, message = " must be shorter")
    private String password;
}
