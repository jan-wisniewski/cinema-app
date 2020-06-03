package wisniewski.jan.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateCinemaRoomDto {
    private String name;
    private Integer cinemaId;
    private Integer rows;
    private Integer places;
}
