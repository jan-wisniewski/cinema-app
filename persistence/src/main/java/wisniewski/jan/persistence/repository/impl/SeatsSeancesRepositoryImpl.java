package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.enums.SeatState;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.model.view.ReservationWithNoUser;
import wisniewski.jan.persistence.model.view.SeatsSeanceWithSeanceDate;
import wisniewski.jan.persistence.repository.SeatsSeancesRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        var sqlSelect = """
                select * from seats_seances where seance_id = :seanceId
                """;

        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sqlSelect)
                        .bind("seanceId", seance.getId())
                        .mapToBean(SeatsSeance.class)
                        .list()
                        .size()
                );
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

    @Override
    public List<SeatsSeanceWithSeanceDate> isOneOfAPlaceReservedForFutureSeance(List<Seat> seats) {
        List<Integer> seatsId = seats.stream().map(Seat::getId).collect(Collectors.toList());
        var sql = """
                select seat_id, state, ss.state as seatState, s.date_time
                from seats_seances ss join seances s on ss.seance_id = s.id
                where seat_id in (<seatsId>)
                        """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bindList("seatsId", seatsId)
                        .mapToBean(SeatsSeanceWithSeanceDate.class)
                        .list()
                        .stream()
                        .filter(s -> !s.getSeatState().equals(SeatState.FREE))
                        .collect(Collectors.toList())
                );
    }

    @Override
    public Integer clearSeatSeances() {
        List<ReservationWithNoUser> seatsSeancesIdToClear;
        var SQL = """
                select ss.id as seatSeanceId,ss.state,r.user_id
                from seats_seances ss
                left join reservations r on ss.seat_id = r.seat_id and ss.seance_id and r.seat_id
                where state = 'RESERVED' and r.user_id is null;
                """;

        seatsSeancesIdToClear = dbConnection
                .getJdbi()
                .withHandle(handle -> handle.createQuery(SQL)
                        .mapToBean(ReservationWithNoUser.class)
                        .list()
                );

        if (seatsSeancesIdToClear.isEmpty()){
            return 0;
        }

        List<Integer> ids = seatsSeancesIdToClear
                .stream()
                .map(ReservationWithNoUser::getSeatSeanceId)
                .collect(Collectors.toList());

        var SQL_UPDATE = """
                update seats_seances set state ='FREE' where id in (<ids>)
                """;

        dbConnection
                .getJdbi()
                .useHandle(handle -> handle
                        .createUpdate(SQL_UPDATE)
                        .bindList("ids", ids)
                        .execute()
                );

        return seatsSeancesIdToClear.size();

    }
}
