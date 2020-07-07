package wisniewski.jan.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO
// czy ta tabela jest potrzebna ?
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Reservation {
    private Integer id;
    private Integer userId;
    private Integer seanceId;
    private Integer seatId;
}
