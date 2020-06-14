package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.model.Ticket;
import wisniewski.jan.persistence.repository.TicketRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

public class TicketRepositoryImpl extends AbstractCrudRepository<Ticket, Integer> implements TicketRepository {

    private DbConnection dbConnection;

    public TicketRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public boolean isPlaceAvailable(CreateTicketDto ticketDto) {
        var seatSeance = dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery("select * from seats_seances where seat_id = :seatId && seance_id = :seanceId && seat_state = 'FREE'")
                        .bind("seatId", ticketDto.getSeatId())
                        .bind("seanceId", ticketDto.getSeanceId())
                        .mapToBean(SeatsSeance.class)
                        .findFirst()
                );
        return seatSeance.isEmpty();
    }
}
