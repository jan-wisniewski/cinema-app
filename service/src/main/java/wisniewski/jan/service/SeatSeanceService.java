package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.repository.SeatsSeancesRepository;
import wisniewski.jan.service.exception.SeatSeanceException;

@RequiredArgsConstructor
public class SeatSeanceService {
    private SeatsSeancesRepository seatsSeancesRepository;

    public SeatsSeance getSeatSeances (Integer seatId){
        return seatsSeancesRepository.findBySeatId(seatId).orElseThrow(() -> new SeatSeanceException("Failed2"));
    }

}
