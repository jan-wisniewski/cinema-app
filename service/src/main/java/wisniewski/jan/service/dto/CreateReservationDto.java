package wisniewski.jan.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateReservationDto {
    private Integer userId;
    private Integer seanceId;
    private Integer seatId;
}
