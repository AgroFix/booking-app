package my.petproject.booking.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import my.petproject.booking.validation.PasswordsAreEqual;

@Data
@Accessors(chain = true)
@PasswordsAreEqual
public class UserRequestRegistrationDto {

    @Email(message = " is incorrect")
    private String email;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @Size(min = 8, message = " must be longer")
    @Size(max = 40, message = " must be shorter")
    private String password;

    @NotBlank
    private String repeatPassword;
}
