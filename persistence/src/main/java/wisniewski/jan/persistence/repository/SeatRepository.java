package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.List;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
    Integer addAll(List<Seat> seatList);

    List<Seat> findAllByCinemaId(CinemaRoom cinemaRoom);

    Integer removeAll(CinemaRoom cinemaRoom, Integer lastRows);
}
