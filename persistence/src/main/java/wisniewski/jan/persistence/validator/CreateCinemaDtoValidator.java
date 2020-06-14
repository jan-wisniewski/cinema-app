package wisniewski.jan.persistence.validator;

import wisniewski.jan.persistence.dto.CreateCinemaDto;
import wisniewski.jan.persistence.validator.validator.AbstractValidator;

import java.util.Map;

public class CreateCinemaDtoValidator extends AbstractValidator<CreateCinemaDto> {
    @Override
    public Map<String, String> validate(CreateCinemaDto item) {
        errors.clear();
        if (isCinemaNameIsLowercase(item)) {
            errors.put("Name", "should starts with uppercase");
        }
        return errors;
    }

    private boolean isCinemaNameIsLowercase(CreateCinemaDto cinemaDto) {
        return cinemaDto.getName().matches("[a-z]+");
    }

}
