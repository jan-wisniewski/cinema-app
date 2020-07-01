package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
    List<Seat> addAll(List<Seat> seatList);

    List<Seat> findAllByCinemaId(CinemaRoom cinemaRoom);

    Integer deleteSeatsByLastRowNumber(CinemaRoom cinemaRoom);

    Integer deleteSeatsByNumberInRow(CinemaRoom cinemaRoom);

//    List<Seat> findSeatsByLastRowNumber(Integer cinemaRoomId, Integer lastRow);
//
//    Boolean isOrderedOrReservedForSeance (Integer seatId);

    Optional<Seat> findByRowAndPlaceAtCinemaRoom(Integer row, Integer place, Integer cinemaRoomId);



}
