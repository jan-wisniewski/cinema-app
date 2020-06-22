package wisniewski.jan.persistence.validator;

import wisniewski.jan.persistence.dto.CreateMovieDto;
import wisniewski.jan.persistence.validator.validator.AbstractValidator;

import java.util.Map;

public class CreateMovieDtoValidator extends AbstractValidator<CreateMovieDto> {

    @Override
    public Map<String, String> validate(CreateMovieDto item) {
        errors.clear();
        if (isTitleLowercase(item)) {
            errors.put("Title", "Should starts with uppercase");
        }
        if (isTitleEmpty(item)) {
            errors.put("Title", "Title cannot be empty");
        }
        if (isDateToBeforeDateFrom(item)) {
            errors.put("Date", "Date to is before date from");
        }
        return errors;
    }

    private boolean isDateToBeforeDateFrom(CreateMovieDto item) {
        return item.getDateTo().isBefore(item.getDateFrom());
    }

    private boolean isTitleLowercase(CreateMovieDto item) {
        return item.getTitle().matches("[a-z]+");
    }

    private boolean isTitleEmpty(CreateMovieDto item) {
        return item.getTitle().equals("");
    }

}
