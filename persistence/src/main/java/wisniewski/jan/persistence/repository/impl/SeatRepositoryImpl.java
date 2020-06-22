package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.SeatRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.List;

public class SeatRepositoryImpl extends AbstractCrudRepository<Seat, Integer> implements SeatRepository {
    private DbConnection dbConnection;

    public SeatRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }


    @Override
    public Integer addAll(List<Seat> seatList) {
        var sql = """
                insert into seats (rows_number, place, cinema_room_id) values (:rows_number,:place,:cinema_room_id)
                """;
        dbConnection
                .getJdbi()
                .withHandle(handle -> {
                    var batch = handle.prepareBatch(sql);
                    seatList.forEach(seat -> batch
                            .bind("rows_number", seat.getRowsNumber())
                            .bind("place", seat.getPlace())
                            .bind("cinema_room_id", seat.getCinemaRoomId())
                            .add()
                    );
                    return batch.execute();
                });
        var selectSql = """
                select * from seats where cinema_room_id = :cinemaRoomId
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(selectSql)
                        .bind("cinemaRoomId", seatList.get(0).getCinemaRoomId())
                        .mapToBean(Seat.class)
                        .list()
                        .size()
                );
    }

    @Override
    public List<Seat> findAllByCinemaId(CinemaRoom cinemaRoom) {
        var SQL = "select * from seats where cinema_id = :cinemaId";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(SQL)
                        .bind("cinema_id", cinemaRoom.getCinemaId())
                        .mapToBean(Seat.class)
                        .list()
                );
    }

    @Override
    public Integer removeAll(CinemaRoom cinemaRoom, Integer lastRow) {
        var SQL = "delete from seats where cinema_id=:cinemaRoomId and rows > lastRow";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createUpdate(SQL)
                        .bind("cinema_id", cinemaRoom.getCinemaId())
                        .execute()
                );
    }
}
