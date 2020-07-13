package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.dto.CreateUserDto;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.repository.UserRepository;
import wisniewski.jan.service.validator.CreateUserDtoValidator;
import wisniewski.jan.service.exception.UserServiceException;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById (Integer userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserServiceException("FAILED"));
    }

    public User findByUsername (String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserServiceException("FAILED"));
    }

    public Integer createUser(CreateUserDto userDto) {
        if (Objects.isNull(userDto)){
            throw new UserServiceException("User Dto is null");
        }
        var userDtoValidator = new CreateUserDtoValidator();
        var errors = userDtoValidator.validate(userDto);
        if (!errors.isEmpty()) {
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
