package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.service.dto.CreateCinemaDto;
import wisniewski.jan.service.exception.AdminServiceException;
import wisniewski.jan.service.exception.CinemaServiceException;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.service.validator.CreateCinemaDtoValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final SeanceRepository seanceRepository;
    private final CinemaRoomRepository cinemaRoomRepository;

    public List<Cinema> getAll() {
        return cinemaRepository.findAll();
    }

    public Optional<Cinema> editCinema(Cinema cinema) {
        return cinemaRepository.update(cinema);
    }

    public Integer deleteCinema(Cinema cinema) {
        if (!seanceRepository.findByCinema(cinema).isEmpty()) {
            System.out.println("Can't delete cinema! Seances will be displayed there!");
            return 0;
        }
        System.out.println("Deleted cinemas rooms associated with this cinema: " +cinemaRoomRepository.deleteAllByCinemaId(cinema));
        return (cinemaRepository.deleteById(cinema.getId())) ? 1 : 0;
    }

    public Integer addCinema(CreateCinemaDto cinemaDto) {
        if (cinemaDto == null) {
            throw new AdminServiceException("Cinema dto is null");
        }
        var validator = new CreateCinemaDtoValidator();
        var errors = validator.validate(cinemaDto);
        if (!errors.isEmpty()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add cinema errors:  " + errorsMessage);
        }
        if (cinemaRepository.findByName(cinemaDto.getName()).isPresent()) {
            throw new AdminServiceException("Cinema with this name is already on DB");
        }

        var cinema = Mapper.fromCinemaDtoToCinema(cinemaDto);
        var createdCinema = cinemaRepository
                .add(cinema)
                .orElseThrow(() -> new AdminServiceException("cannot insert to db"));
        return createdCinema.getId();
    }

    public String showAllCinemas() {
        return cinemaRepository.findAll()
                .stream()
                .map(cinema -> cinema.getId() + ". " + cinema.getName())
                .collect(Collectors.joining("\n"));
    }

    public String getNameByCinemaId(Integer cinemaId) {
        return cinemaRepository.findById(cinemaId).orElseThrow(() -> new CinemaServiceException("Failed341")).getName();
    }

    public Optional<Cinema> findCinemaById(Integer cinemaId) {
        return cinemaRepository.findById(cinemaId);
    }

    public Optional<Cinema> findCinemaByCityId(Integer cityId) {
        return cinemaRepository.findByCityId(cityId);
    }

    public Cinema findByCinemaRoomId(Integer cinemaRoomId) {
        return cinemaRepository
                .findByCinemaRoomId(cinemaRoomId)
                .orElseThrow(() -> new CinemaServiceException("Failed"));
    }
}
