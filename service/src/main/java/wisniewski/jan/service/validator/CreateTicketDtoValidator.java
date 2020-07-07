package wisniewski.jan.service.validator;

import wisniewski.jan.service.validator.validator.Validator;
import wisniewski.jan.service.dto.CreateTicketDto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CreateTicketDtoValidator  implements Validator<CreateTicketDto> {
    @Override
    public Map<String, String> validate(CreateTicketDto item) {
        var errors = new HashMap<String, String>();
        if (!isPricePositive(item)) {
            errors.put("Price", "Price should be positive");
        }
        if (isDiscountNegative(item)) {
            errors.put("Discount", "Discount can't be negative");
        }
        return errors;
    }

    private boolean isPricePositive(CreateTicketDto ticketDto) {
        return ticketDto.getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isDiscountNegative(CreateTicketDto ticketDto) {
        return ticketDto.getDiscount().compareTo(BigDecimal.ZERO) < 0;
    }

}
