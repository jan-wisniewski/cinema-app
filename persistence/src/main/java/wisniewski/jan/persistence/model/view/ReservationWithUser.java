package wisniewski.jan.persistence.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationWithUser {
    private Integer reservationId;
    private Integer seanceId;
    private Integer userId;
    private Integer seatId;
    private String userEmail;
}
