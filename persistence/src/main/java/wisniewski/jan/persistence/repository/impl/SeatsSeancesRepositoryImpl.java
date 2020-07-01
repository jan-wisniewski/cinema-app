package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.enums.SeatState;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.repository.SeatsSeancesRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;

public class SeatsSeancesRepositoryImpl extends AbstractCrudRepository<SeatsSeance, Integer> implements SeatsSeancesRepository {

    private DbConnection dbConnection;

    public SeatsSeancesRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public Integer addAll(List<Seat> seats, Seance seance) {
        var sql = """
                insert into seats_seances (seat_id, seance_id,state) values (:seat_id,:seance_id,:state)
                """;
        dbConnection
                .getJdbi()
                .withHandle(handle -> {
                            var batch = handle.prepareBatch(sql);
                            seats.forEach(seat -> batch
                                    .bind("seat_id", seat.getId())
                                    .bind("seance_id", seance.getId())
                                    .bind("state", SeatState.FREE)
                                    .add()
                            );
                            return batch.execute();
                        }
                );
        return 1;
    }

    @Override
    public Optional<SeatsSeance> findBySeatId(Integer seatId) {
        var sql = "select * from seats_seances where seat_id = :seat_id";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seat_id", seatId)
                        .mapToBean(SeatsSeance.class)
                        .findFirst()
                );
    }

    @Override
    public List<SeatsSeance> findBySeanceId(Integer seanceId) {
        var sql = """
                select * from seats_seances where seance_id = :seance_id;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("seance_id", seanceId)
                        .mapToBean(SeatsSeance.class)
                        .list()
                );
    }

    @Override
    public List<SeatsSeance> addAllBySeanceId(List<Seat> seats, Integer seanceId) {
        var sql = """
                insert into seats_seances (seat_id, seance_id,state) values (:seat_id,:seance_id,:state);
                """;
        seats.forEach(s -> dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createUpdate(sql)
                        .bind("seat_id", s.getId())
                        .bind("seance_id", seanceId)
                        .bind("state", SeatState.FREE)
                        .execute()
                )
        );
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery("select * from seats_seances where seance_id = :seance_id")
                        .bind("seance_id", seanceId)
                        .mapToBean(SeatsSeance.class)
                        .list()
                );
    }
}
