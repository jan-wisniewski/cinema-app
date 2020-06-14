package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.dto.CreateUserDto;
import wisniewski.jan.persistence.mappers.Mapper;
import wisniewski.jan.persistence.repository.UserRepository;
import wisniewski.jan.persistence.validator.CreateUserDtoValidator;
import wisniewski.jan.service.exception.UserServiceException;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Integer createUser(CreateUserDto userDto) {
        if (Objects.isNull(userDto)){
            throw new UserServiceException("User Dto is null");
        }
        var userDtoValidator = new CreateUserDtoValidator();
        var errors = userDtoValidator.validate(userDto);
        if (userDtoValidator.hasErrors()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new UserServiceException("create user validation errors: " + errorsMessage);
        }
        var user = Mapper.fromCreateUserDtoToUser(userDto);
        var addedUser = userRepository
                .add(user)
                .orElseThrow(() -> new UserServiceException("cannot insert user to db"));
        return addedUser.getId();
    }
}
