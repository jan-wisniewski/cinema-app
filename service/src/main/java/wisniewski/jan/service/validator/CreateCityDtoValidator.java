package wisniewski.jan.service.validator;

import wisniewski.jan.service.validator.validator.Validator;
import wisniewski.jan.service.dto.CreateCityDto;

import java.util.HashMap;
import java.util.Map;

public class CreateCityDtoValidator  implements Validator<CreateCityDto> {

    @Override
    public Map<String, String> validate(CreateCityDto item) {
        var errors = new HashMap<String, String>();
        if (isStartsLowercase(item)) {
            errors.put("City","Name should starts from uppercase");
        }
        return errors;
    }

    private boolean isStartsLowercase(CreateCityDto cityDto) {
        return cityDto.getName().matches("[a-z].+");
    }

}
