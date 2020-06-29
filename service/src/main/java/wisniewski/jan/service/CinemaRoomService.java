package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.service.exception.CinemaRoomException;

import java.util.List;

@RequiredArgsConstructor
public class CinemaRoomService {

    private final CinemaRoomRepository cinemaRoomRepository;
    private final SeanceRepository seanceRepository;

    public String getName(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId).orElseThrow(() -> new CinemaRoomException("Failed")).getName();
    }

    public Integer getRowsNumber(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId).orElseThrow(() -> new CinemaRoomException("Failed")).getRowsNumber();
    }

    public CinemaRoom getCinemaRoom(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId).orElseThrow(() -> new CinemaRoomException("Failed"));
    }

    public List<CinemaRoom> getCinemaRoomList(Integer cinemaId) {
        return cinemaRoomRepository.findByCinemaId(cinemaId);
    }

}