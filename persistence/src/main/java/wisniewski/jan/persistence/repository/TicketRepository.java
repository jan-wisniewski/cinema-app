package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.Ticket;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    boolean isPlaceAvailable(Ticket ticket);
    List<Ticket> findBySeance (Seance seance);
}
