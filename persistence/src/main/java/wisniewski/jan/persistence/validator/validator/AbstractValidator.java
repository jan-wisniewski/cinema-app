package wisniewski.jan.persistence.validator.validator;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractValidator<T> implements Validator<T> {
    protected Map<String, String> errors = new HashMap<>();
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
