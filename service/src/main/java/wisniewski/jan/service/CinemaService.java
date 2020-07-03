package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.service.exception.CinemaServiceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public List<Cinema> getAll() {
        return cinemaRepository.findAll();
    }

    public String showAllCinemas (){
        return cinemaRepository.findAll()
                .stream()
                .map(cinema -> cinema.getId() + ". " + cinema.getName())
                .collect(Collectors.joining("\n"));
    }

    public String getNameByCinemaId(Integer cinemaId) {
        return cinemaRepository.findById(cinemaId).orElseThrow(() -> new CinemaServiceException("Failed341")).getName();
    }

    public Optional<Cinema> findCinemaById (Integer cinemaId){
        return cinemaRepository.findById(cinemaId);
    }

    public Optional<Cinema> findCinemaByCityId (Integer cityId){
        return cinemaRepository.findByCityId(cityId);
    }

}
