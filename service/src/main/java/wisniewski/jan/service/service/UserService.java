package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.enums.Role;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.dto.CreateUserDto;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.repository.UserRepository;
import wisniewski.jan.service.validator.CreateUserDtoValidator;
import wisniewski.jan.service.exception.UserServiceException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById (Integer userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserServiceException("FAILED"));
    }

    public List<User> findAll(){
        return userRepository.findAll();
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
        user.setRole(Role.USER);
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UserServiceException("user with this username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserServiceException("user with this email already exists");
        }
        var addedUser = userRepository
                .add(user)
                .orElseThrow(() -> new UserServiceException("cannot insert user to db"));
        return addedUser.getId();
    }

    public User edit(User chosenUser) {
        return userRepository.update(chosenUser).orElseThrow(() -> new UserServiceException("Cannot edit user"));
    }

    public boolean delete(Integer id) {
        return userRepository.deleteById(id);
    }
}
