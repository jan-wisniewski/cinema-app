package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Reservation;
import wisniewski.jan.persistence.model.ReservationWithUser;
import wisniewski.jan.persistence.repository.ReservationRepository;
import wisniewski.jan.service.exception.ReservationServiceException;

import java.util.List;

@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<ReservationWithUser> showReservations(String email) {
        return reservationRepository.findByEmail(email);
    }

    public Reservation getReservation (Integer reservationId){
        return reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationServiceException("Failed"));
    }


}
