package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.model.Ticket;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    boolean isPlaceAvailable(CreateTicketDto ticketDto);
}
