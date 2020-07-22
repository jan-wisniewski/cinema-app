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
            errors.put("Email", "E-mail is not valid");
        }
        if (!isMatchingPassword(item)){
            errors.put("Password", "Passwords are not the same");
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
        return userDto.getEmail().matches("([A-Za-z0-9]\\.?)+@[A-Za-z0-9]+\\.[a-z]+");
    }

    private boolean isMatchingPassword (CreateUserDto userDto){
        return userDto.getPassword().equals(userDto.getRepeatedPassword());
    }
}
