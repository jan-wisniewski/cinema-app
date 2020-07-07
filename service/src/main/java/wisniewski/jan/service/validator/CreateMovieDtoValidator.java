package wisniewski.jan.service.validator;

import wisniewski.jan.service.validator.validator.Validator;
import wisniewski.jan.service.dto.CreateMovieDto;

import java.util.HashMap;
import java.util.Map;

public class CreateMovieDtoValidator  implements Validator<CreateMovieDto> {

    @Override
    public Map<String, String> validate(CreateMovieDto item) {
        var errors = new HashMap<String, String>();
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
