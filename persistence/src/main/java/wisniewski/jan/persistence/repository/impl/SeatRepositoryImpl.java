package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.SeatRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SeatRepositoryImpl extends AbstractCrudRepository<Seat, Integer> implements SeatRepository {
    private DbConnection dbConnection;

    public SeatRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }


    @Override
    public List<Seat> addAll(List<Seat> seatList) {
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
    public Integer deleteAll(List<Seat> seats) {
        List<Integer> seatsIds = seats.stream().map(Seat::getId).collect(Collectors.toList());
        var sql = """
                delete from seats where id in (<seatsId>)
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createUpdate(sql)
                        .bindList("seatsId", seatsIds)
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

    @Override
    public List<Seat> findByPlacesAboveSeatPlace(CinemaRoom cinemaRoom, Integer seatPlaceNumber) {
        var sql = """
                select * from seats where cinema_room_id = :cinemaRoomId and place > :seatPlace
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("cinemaRoomId", cinemaRoom.getId())
                        .bind("seatPlace", cinemaRoom.getPlaces())
                        .mapToBean(Seat.class)
                        .list()
                );
    }

    @Override
    public List<Seat> findByPlacesAboveSeatRow(CinemaRoom cinemaRoom, Integer seatRowNumber) {
        var sql = """
                  select * from seats where cinema_room_id = :cinema_room_id and rows_number > :last_row;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("cinema_room_id", cinemaRoom.getId())
                        .bind("last_row", seatRowNumber)
                        .mapToBean(Seat.class)
                        .list()
                );
    }
}
