package wisniewski.jan.persistence.validator;

import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.validator.validator.AbstractValidator;

import java.math.BigDecimal;
import java.util.Map;

public class CreateTicketDtoValidator extends AbstractValidator<CreateTicketDto> {
    @Override
    public Map<String, String> validate(CreateTicketDto item) {
        errors.clear();
        if (!isPricePositive(item)) {
            errors.put("Price", "Price should be positive");
        }
        if (!isDiscountPositive(item)) {
            errors.put("Discount", "Discount be positive");
        }
        return errors;
    }

    private boolean isPricePositive(CreateTicketDto ticketDto) {
        return ticketDto.getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isDiscountPositive(CreateTicketDto ticketDto) {
        return ticketDto.getDiscount().compareTo(BigDecimal.ZERO) > 0;
    }

}
