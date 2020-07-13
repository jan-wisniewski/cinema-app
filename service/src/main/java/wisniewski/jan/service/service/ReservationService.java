package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Reservation;
import wisniewski.jan.persistence.model.view.ReservationWithUser;
import wisniewski.jan.persistence.repository.ReservationRepository;
import wisniewski.jan.service.exception.ReservationServiceException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Integer clearReservations(Integer minutes) {
        return reservationRepository.deleteIfLessThanMinutes(minutes);
    }

    public List<ReservationWithUser> showReservationsListByEmail(String email) {
        return reservationRepository.findByEmail(email);
    }

    public Reservation getReservationByReservationId(Integer reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationServiceException("Failed"));
    }

    public Optional<Reservation> addReservation(Reservation reservation) {
        return reservationRepository.add(reservation);
    }

    public boolean deleteReservation(Integer reservationId) {
        return reservationRepository.deleteById(reservationId);
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }
}
