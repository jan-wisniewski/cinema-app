package wisniewski.jan.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateUserDto {
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String repeatedPassword;
}
