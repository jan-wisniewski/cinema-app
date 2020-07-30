package wisniewski.jan.persistence.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wisniewski.jan.persistence.enums.SeatState;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationWithNoUser {
    private Integer seatSeanceId;
    private SeatState state;
    private Integer userId;
}
