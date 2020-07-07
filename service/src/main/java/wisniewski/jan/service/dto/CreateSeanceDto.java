package wisniewski.jan.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateSeanceDto {
    private Integer movieId;
    private Integer cinemaRoomId;
    private LocalDateTime dateTime;
}
