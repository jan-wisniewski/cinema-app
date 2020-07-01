package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SeatsSeancesRepository extends CrudRepository<SeatsSeance, Integer> {
    Integer addAll(List<Seat> seats, Seance seance);
    Optional<SeatsSeance> findBySeatId(Integer seatId);
    List<SeatsSeance> findBySeanceId(Integer seanceId);
    List<SeatsSeance> addAllBySeanceId (List<Seat> seats, Integer seanceId);
}
