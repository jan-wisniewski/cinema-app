package wisniewski.jan.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateUserDto {
    private String name;
    private String surname;
    private String email;
    private String password;
    private Role role;
}
