package wisniewski.jan.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Seance {
    private Integer id;
    private Integer movieId;
    private Integer cinemaRoomId;
    private LocalDateTime dateTime;
    private BigDecimal price;
}
