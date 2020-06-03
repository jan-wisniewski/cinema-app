package wisniewski.jan.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CinemaRoom {
    private Integer id;
    private String name;
    private Integer cinemaId;
    private Integer rows;
    private Integer places;
}
