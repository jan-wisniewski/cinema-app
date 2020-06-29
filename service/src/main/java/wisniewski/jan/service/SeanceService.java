package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.service.exception.SeanceServiceException;

import java.util.List;

@RequiredArgsConstructor
public class SeanceService {

    private final SeanceRepository seanceRepository;

    public Seance getSeance(Integer seanceId) {
        return seanceRepository.findById(seanceId)
                .orElseThrow(() -> new SeanceServiceException("Failed"));
    }

    public List<Seance> getSeancesList(List<CinemaRoom> cinemaRooms) {
        return seanceRepository.findSeancesByCinemaRooms(cinemaRooms);
    }


}
