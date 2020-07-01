package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.repository.SeatsSeancesRepository;
import wisniewski.jan.service.exception.SeatSeanceException;

import java.util.List;

@RequiredArgsConstructor
public class SeatSeanceService {
    private final SeatsSeancesRepository seatsSeancesRepository;

    public SeatsSeance getSeatSeancesBySeatId(Integer seatId) {
        return seatsSeancesRepository.findBySeatId(seatId).orElseThrow(() -> new SeatSeanceException("Failed2"));
    }

    public List<SeatsSeance> getSeatsSeancesListBySeanceId(Integer seanceId) {
        return seatsSeancesRepository.findBySeanceId(seanceId);
    }

    public List<SeatsSeance> addAllBySeanceId (List<Seat> seats, Integer seanceId) {
        return seatsSeancesRepository.addAllBySeanceId(seats,seanceId);
    }

}
