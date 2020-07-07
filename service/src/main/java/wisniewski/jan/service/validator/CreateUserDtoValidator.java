package wisniewski.jan.service.validator;

import wisniewski.jan.service.validator.validator.Validator;
import wisniewski.jan.service.dto.CreateUserDto;

import java.util.HashMap;
import java.util.Map;

public class CreateUserDtoValidator  implements Validator<CreateUserDto> {

    @Override
    public Map<String, String> validate(CreateUserDto item) {
        var errors = new HashMap<String, String>();
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
