package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.service.exception.CinemaServiceException;

import java.util.List;

@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public List<Cinema> findAll() {
        return cinemaRepository.findAll();
    }

    public String getName(Integer cinemaId) {
        return cinemaRepository.findById(cinemaId).orElseThrow(() -> new CinemaServiceException("Failed")).getName();
    }

}
