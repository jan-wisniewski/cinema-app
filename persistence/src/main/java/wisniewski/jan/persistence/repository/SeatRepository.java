package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
    List<Seat> addAll(List<Seat> seatList);

    List<Seat> findAllByCinemaId(CinemaRoom cinemaRoom);

    Optional<Seat> findByRowAndPlaceAtCinemaRoom(Integer row, Integer place, Integer cinemaRoomId);

    List<Seat> findByPlacesAboveSeatPlace(CinemaRoom cinemaRoom, Integer seatPlaceNumber);

    List<Seat> findByPlacesAboveSeatRow(CinemaRoom cinemaRoom, Integer seatRowNumber);

    Integer deleteAll(List<Seat> seats);


}
