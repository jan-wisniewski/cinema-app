package wisniewski.jan.service.service.proxy;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.enums.Role;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.dto.AuthenticationDto;
import wisniewski.jan.service.exception.AuthenticationException;
import wisniewski.jan.persistence.repository.UserRepository;

import java.util.Objects;

@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private User user;

    public String login(AuthenticationDto authenticationDto) {
        var user = userRepository
                .findByUsername(authenticationDto.getUsername())
                .orElseThrow(() -> new AuthenticationException("Cannot find user in db"));
        if (!Objects.equals(user.getPassword(), authenticationDto.getPassword())) {
            throw new AuthenticationException("Password is  not correct");
        }

        this.user = user;
        return this.user.getUsername();
    }

    public String logout() {
        var username = this.user.getUsername();
        this.user = null;
        return username;
    }

    public boolean isUser() {
        return this.user != null && this.user.getRole().equals(Role.USER);
    }

    public boolean isAdmin() {
        return this.user != null && this.user.getRole().equals(Role.ADMIN);
    }
}
