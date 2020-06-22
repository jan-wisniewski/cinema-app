package wisniewski.jan.persistence.validator;

import wisniewski.jan.persistence.dto.CreateSeanceDto;
import wisniewski.jan.persistence.validator.validator.AbstractValidator;

import java.time.LocalDateTime;
import java.util.Map;

public class CreateSeanceDtoValidator extends AbstractValidator<CreateSeanceDto> {

    @Override
    public Map<String, String> validate(CreateSeanceDto item) {
        errors.clear();
        if (isLocalDateHasAlreadyPassed(item)) {
            errors.put("Local Date", "Time is before current date");
        }
        return errors;
    }

    private boolean isLocalDateHasAlreadyPassed(CreateSeanceDto seanceDto) {
        return seanceDto.getDateTime().isBefore(LocalDateTime.now());
    }

}
