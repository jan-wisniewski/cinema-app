package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.SeatRepository;
import wisniewski.jan.service.exception.SeatServiceException;

@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public Seat getSeat(Integer seatId) {
        return seatRepository.findById(seatId).orElseThrow(() -> new SeatServiceException("Failed"));
    }

    public Integer getSeatId(Integer rowNumber, Integer placeNumber, Integer cinemaRoomId) {
        return seatRepository
                .findByRowAndPlaceAtCinemaRoom(rowNumber, placeNumber, cinemaRoomId)
                .orElseThrow(() -> new SeatServiceException("Failed"))
                .getId();
    }

}
