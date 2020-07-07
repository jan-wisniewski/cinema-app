package wisniewski.jan.service.validator;


import wisniewski.jan.service.validator.validator.Validator;
import wisniewski.jan.service.dto.CreateSeanceDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CreateSeanceDtoValidator  implements Validator<CreateSeanceDto> {

    @Override
    public Map<String, String> validate(CreateSeanceDto item) {
        var errors = new HashMap<String, String>();
        if (isLocalDateHasAlreadyPassed(item)) {
            errors.put("Local Date", "Time is before current date");
        }
        return errors;
    }

    private boolean isLocalDateHasAlreadyPassed(CreateSeanceDto seanceDto) {
        return seanceDto.getDateTime().isBefore(LocalDateTime.now());
    }

}
