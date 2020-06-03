package wisniewski.jan.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wisniewski.jan.persistence.enums.SeatState;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateSeatSeanceDto {
    private Integer seatId;
    private Integer seanceId;
    private SeatState state;
}
