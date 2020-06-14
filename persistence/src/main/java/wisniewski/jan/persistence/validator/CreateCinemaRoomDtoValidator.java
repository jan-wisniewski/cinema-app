package wisniewski.jan.persistence.validator;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.dto.CreateCinemaRoomDto;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;
import wisniewski.jan.persistence.validator.validator.AbstractValidator;

import java.util.Map;

public class CreateCinemaRoomDtoValidator extends AbstractValidator<CreateCinemaRoomDto> {

    @Override
    public Map<String, String> validate(CreateCinemaRoomDto item) {
        errors.clear();
        if (isNameLowercase(item)) {
            errors.put("Name", "Should starts with uppercase");
        }
        if (isRowsNegativeOrEqualZero(item)) {
            errors.put("Rows", "Should be greater than 0");
        }
        if (isPlacesNegativeOrEqualZero(item)) {
            errors.put("Places", "Should be greater than 0");
        }
        if (isNameTooShort(item)) {
            errors.put("Name", "Should be longer than one char");
        }
        return errors;
    }

    private boolean isNameLowercase(CreateCinemaRoomDto cinemaRoomDto) {
        return cinemaRoomDto.getName().matches("[a-z]+");
    }

    private boolean isRowsNegativeOrEqualZero(CreateCinemaRoomDto cinemaRoomDto) {
        return cinemaRoomDto.getRows() < 1;
    }

    private boolean isPlacesNegativeOrEqualZero(CreateCinemaRoomDto cinemaRoomDto) {
        return cinemaRoomDto.getPlaces() < 1;
    }

    private boolean isNameTooShort(CreateCinemaRoomDto cinemaRoomDto) {
        return cinemaRoomDto.getName().length() < 2;
    }

}
