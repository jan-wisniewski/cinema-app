package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.model.Ticket;
import wisniewski.jan.persistence.repository.TicketRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TicketRepositoryImpl extends AbstractCrudRepository<Ticket, Integer> implements TicketRepository {

    private DbConnection dbConnection;

    public TicketRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public boolean isPlaceAvailable(Ticket ticket) {
        var seatSeance = dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery("select * from seats_seances where seat_id = :seatId AND seance_id = :seanceId AND seat_state = 'FREE'")
                        .bind("seatId", ticket.getSeatId())
                        .bind("seanceId", ticket.getSeanceId())
                        .mapToBean(SeatsSeance.class)
                        .findFirst()
                );
        return seatSeance.isEmpty();
    }

    @Override
    public List<Ticket> findBySeance(Seance seance) {
        var sql = """
                select t.id, t.seance_id,t.seat_id,t.price,t.discount,t.user_id
                from tickets t join seances s on t.seance_id = s.id
                where seance_id = :seanceId and s.date_time > :date
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seanceId",seance.getId())
                        .bind("date", LocalDateTime.now())
                        .mapToBean(Ticket.class)
                        .list()
                );
    }
}
