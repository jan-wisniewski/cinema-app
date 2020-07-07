package wisniewski.jan.persistence.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wisniewski.jan.persistence.enums.SeatState;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SeatsSeanceWithSeanceDate {
    private Integer id;
    private Integer seatId;
    private SeatState seatState;
    private LocalDateTime dateTime;
}
