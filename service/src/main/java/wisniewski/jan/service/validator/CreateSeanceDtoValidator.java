package wisniewski.jan.service.validator;


import wisniewski.jan.service.validator.validator.Validator;
import wisniewski.jan.service.dto.CreateSeanceDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CreateSeanceDtoValidator implements Validator<CreateSeanceDto> {

    @Override
    public Map<String, String> validate(CreateSeanceDto item) {
        var errors = new HashMap<String, String>();
        if (isLocalDateHasAlreadyPassed(item)) {
            errors.put("Local Date", "Time is before current date");
        }
        if (isPriceIsNegative(item)) {
            errors.put("Price", "Price should be positive");
        }
        return errors;
    }

    private boolean isLocalDateHasAlreadyPassed(CreateSeanceDto seanceDto) {
        return seanceDto.getDateTime().isBefore(LocalDateTime.now());
    }

    private boolean isPriceIsNegative(CreateSeanceDto seanceDto) {
        return seanceDto.getPrice().compareTo(BigDecimal.ZERO) < 0;
    }

}
