package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.service.dto.CreateSeanceDto;
import wisniewski.jan.service.exception.AdminServiceException;
import wisniewski.jan.service.exception.UserDataServiceException;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.service.validator.CreateSeanceDtoValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final MovieRepository movieRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    private final SeatsSeancesRepository seatsSeancesRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public Optional<Seance> editSeance(Seance seance) {
        return seanceRepository.update(seance);
    }

    public Integer addSeance(CreateSeanceDto seanceDto) {
        if (seanceDto == null) {
            throw new AdminServiceException("seance dto is null");
        }
        var validator = new CreateSeanceDtoValidator();
        var errors = validator.validate(seanceDto);
        if (!errors.isEmpty()) {
            String errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add seance errors: " + errorsMessage);
        }

        var seance = Mapper.fromSeanceDtoToSeance(seanceDto);

        if (seanceRepository.isUniqueSeance(seance).isPresent()) {
            throw new AdminServiceException("Seance is not unique. The same movieId, cinemaRoomId i LocalDate");
        }

        if (!seanceRepository.isMovieDisplayed(seance)) {
            throw new AdminServiceException("Screening date is not within the time frame of the movie");
        }

        var addedSeance = seanceRepository
                .add(seance)
                .orElseThrow(() -> new AdminServiceException("cannot insert to db"));

        System.out.println("Seats added to this seances: " + getSeatsForCinemaRoomAndAddToSeance(addedSeance));

        return addedSeance.getId();
    }

    private Integer getSeatsForCinemaRoomAndAddToSeance(Seance seance) {
        int cinemaRoomId = seance.getCinemaRoomId();
        CinemaRoom cinemaRoom = cinemaRoomRepository
                .findById(cinemaRoomId)
                .orElseThrow(() -> new AdminServiceException("Failed"));
        List<Seat> seatsForCinemaRoom = seatRepository.findAllByCinemaId(cinemaRoom);
        return seatsSeancesRepository.addAll(seatsForCinemaRoom, seance);
    }

    public Integer deleteSeance(Seance seance) {
        if (!ticketRepository.findBySeance(seance).isEmpty()) {
            System.out.println("Can't delete seance! Ticket sales have already started");
            return 0;
        }
        return (seanceRepository.deleteById(seance.getId())) ? 1 : 0;
    }

    public Optional<Seance> getSeanceById(Integer seanceId) {
        return seanceRepository.findById(seanceId);
    }

    public List<Seance> getSeancesListByCinemaRooms(List<CinemaRoom> cinemaRooms) {
        return seanceRepository
                .findSeancesByCinemaRooms(cinemaRooms);
    }

    public String showAll() {
        return seanceRepository.findAll()
                .stream()
                .map(seance -> seance.getId() + ". " +
                        movieRepository.findById(seance.getMovieId())
                                .orElseThrow(() -> new UserDataServiceException("FAILED!"))
                                .getTitle()
                )
                .collect(Collectors.joining("\n"));
    }

    public List<Seance> getAll() {
        return seanceRepository.findAll();
    }

    public List<Seance> findSeancesFromDateAtCinemaRoom(Integer cinemaRoomId) {
        return seanceRepository.findFutureSeancesAtCinemaRoom(cinemaRoomId);
    }

    public List<Seance> findByMovie(Movie movie) {
        return seanceRepository.findByMovie(movie);
    }

    public List<Seance> findByPhrase(String phrase) {
        return seanceRepository.findByPhrase(phrase);
    }

    public Optional<Seance> findById(Integer seanceId) {
        return seanceRepository.findById(seanceId);
    }
}
