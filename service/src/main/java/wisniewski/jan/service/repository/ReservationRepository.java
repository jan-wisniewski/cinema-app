package wisniewski.jan.service.repository;

import wisniewski.jan.persistence.model.Reservation;
import wisniewski.jan.persistence.model.view.ReservationWithUser;
import wisniewski.jan.service.repository.generic.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    List<Reservation> findBySeanceId(Integer seanceId);

    List<ReservationWithUser> findByEmail(String email);

    Integer deleteIfLessThanMinutes(Integer minutes);
}
