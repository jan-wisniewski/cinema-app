package wisniewski.jan.service.validator.validator;

import java.util.Map;

public interface Validator<T> {
    Map<String,String> validate (T item);
}
