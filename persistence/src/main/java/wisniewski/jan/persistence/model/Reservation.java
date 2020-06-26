package wisniewski.jan.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
