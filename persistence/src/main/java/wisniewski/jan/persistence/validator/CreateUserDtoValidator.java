package wisniewski.jan.persistence.validator;

import wisniewski.jan.persistence.dto.CreateUserDto;
import wisniewski.jan.persistence.validator.validator.AbstractValidator;

import java.util.Map;

public class CreateUserDtoValidator extends AbstractValidator<CreateUserDto> {

    @Override
    public Map<String, String> validate(CreateUserDto item) {
        errors.clear();
        if (!isNameStartsWithUppercase(item)) {
            errors.put("Name", "Should starts with uppercase");
        }
        if (!isSurnameStartsWithUppercase(item)) {
            errors.put("Surname", "Should starts with uppercase");
        }
        if (!isEmailValid(item)) {
            errors.put("Email", "Should starts with uppercase");
        }
        return errors;
    }

    private boolean isNameStartsWithUppercase(CreateUserDto userDto) {
        return userDto.getName().matches("[A-Z][a-z]+");
    }

    private boolean isSurnameStartsWithUppercase(CreateUserDto userDto) {
        return userDto.getSurname().matches("[A-Z][a-z]+");
    }

    private boolean isEmailValid(CreateUserDto userDto) {
        return userDto.getEmail().matches("([A-Za-z]\\.?)+@[A-Za-z]+\\.[a-z]+");
    }
}
