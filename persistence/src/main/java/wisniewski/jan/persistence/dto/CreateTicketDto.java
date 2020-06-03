package wisniewski.jan.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateTicketDto {
    private Integer row;
    private Integer place;
    private Integer cinemaRoomId;
}
