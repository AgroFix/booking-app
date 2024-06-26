package my.petproject.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import my.petproject.booking.dto.user.UserRequestRegistrationDto;

public class PasswordValidator implements ConstraintValidator<PasswordsAreEqual, Object> {
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        UserRequestRegistrationDto user = (UserRequestRegistrationDto) object;
        return user.getPassword().equals(user.getRepeatPassword());
    }
}
