package wisniewski.jan.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateTicketDto {
    private Integer seanceId;
    private Integer seatId;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer userId;
}
