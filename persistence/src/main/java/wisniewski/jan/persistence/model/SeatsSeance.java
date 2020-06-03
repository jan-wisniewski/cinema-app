package wisniewski.jan.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wisniewski.jan.persistence.enums.SeatState;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SeatsSeance {
    private Integer id;
    private Integer seatId;
    private Integer seanceId;
    private SeatState state;
}
