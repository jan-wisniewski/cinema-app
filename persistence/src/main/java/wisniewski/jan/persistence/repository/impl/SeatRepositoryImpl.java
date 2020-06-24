package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.SeatRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;

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
        var SQL = "select * from seats where cinema_room_id = :cinema_room_id";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(SQL)
                        .bind("cinema_room_id", cinemaRoom.getId())
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

    @Override
    public Optional<Seat> findByRowAndPlaceAtCinemaRoom(Integer row, Integer place, Integer cinemaRoomId) {
        var sql = "select * from seats where rows_number = :rows_number AND place=:place AND cinema_room_id = :cinema_room_id";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("rows_number", row)
                        .bind("place", place)
                        .bind("cinema_room_id", cinemaRoomId)
                        .mapToBean(Seat.class)
                        .findFirst()
                );
    }
}
