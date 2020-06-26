package wisniewski.jan.persistence.validator;

import wisniewski.jan.persistence.dto.CreateCityDto;
import wisniewski.jan.persistence.model.City;
import wisniewski.jan.persistence.validator.validator.AbstractValidator;

import java.util.Map;

public class CreateCityDtoValidator extends AbstractValidator<CreateCityDto> {

    @Override
    public Map<String, String> validate(CreateCityDto item) {
        errors.clear();
        if (isStartsLowercase(item)) {
            errors.put("City","Name should starts from uppercase");
        }
        return errors;
    }

    private boolean isStartsLowercase(CreateCityDto cityDto) {
        return cityDto.getName().matches("[a-z].+");
    }

}
