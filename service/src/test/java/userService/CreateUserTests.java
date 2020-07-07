package userService;

import extensions.LoggerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.apache.log4j.Logger;
import wisniewski.jan.service.dto.CreateUserDto;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.repository.UserRepository;
import wisniewski.jan.service.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoggerExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CreateUserTests {

    private Logger logger;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Test
    @DisplayName("when user name starts with lowercase exception thrown")
    public void test1() {
        CreateUserDto userDto = CreateUserDto
                .builder()
                .name("adam")
                .surname("Kowalski")
                .email("a@a.pl")
                .password("123")
                .build();
        String exceptionMessage = "";
        try {
            userService.createUser(userDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("create user validation errors: Name : Should starts with uppercase", exceptionMessage);
        logger.info("Failed to create user: lowercase exception");
    }

    @Test
    @DisplayName("when user data is correct returns id")
    public void test2() {
        CreateUserDto userDto = CreateUserDto
                .builder()
                .name("Adam")
                .surname("Kowalski")
                .email("a@a.pl")
                .password("123")
                .build();

        var user = Mapper.fromCreateUserDtoToUser(userDto);

        User expected = User
                .builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email("a@a.pl")
                .password("123")
                .id(1)
                .build();

        Mockito
                .when(userRepository.add(user))
                .thenReturn(Optional.of(expected));

        assertEquals(expected.getId(), userService.createUser(userDto));
        logger.info("USER CREATED SUCCESSFULLY");

    }

    @Test
    @DisplayName("when surname starts with lowercase exception thrown")
    public void test3() {
        var userDto = CreateUserDto
                .builder()
                .password("123")
                .email("adam@adam.pl")
                .surname("malinowski")
                .name("Adam")
                .build();
        String exceptionMessage = "";
        try {
            userService.createUser(userDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("create user validation errors: Surname : Should starts with uppercase", exceptionMessage);
        logger.info("Failed to create user: lowercase exception");
    }

    @Test
    @DisplayName("when email is invalid exception thrown")
    public void test4() {
        var userDto = CreateUserDto
                .builder()
                .email("aa.pl")
                .name("Adam")
                .surname("Malinowski")
                .password("123")
                .build();
        String exceptionMessage = "";
        try {
            userService.createUser(userDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        System.out.println(exceptionMessage);
        assertEquals("create user validation errors: Email : Should starts with uppercase", exceptionMessage);
        logger.info("Failed to create user: email exception");
    }

    @Test
    @DisplayName("when dto is null exception thrown")
    public void test5() {
        String exceptionMessage = "";
        try {
            userService.createUser(null);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        System.out.println(exceptionMessage);
        assertEquals("User Dto is null", exceptionMessage);
        logger.info("Failed to create user: null dto exception");
    }
}