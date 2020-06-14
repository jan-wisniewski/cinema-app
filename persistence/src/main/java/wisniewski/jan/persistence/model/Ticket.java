package wisniewski.jan.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ticket {
    private Integer id;
    private Integer seanceId;
    private Integer seatId;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer userId;
}