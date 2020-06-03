package wisniewski.jan.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ticket {
    private Integer id;
    private Integer row;
    private Integer place;
    private Integer cinemaRoomId;
}
