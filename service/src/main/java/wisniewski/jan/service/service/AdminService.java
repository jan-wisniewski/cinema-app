package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.service.dto.*;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.service.repository.*;
import wisniewski.jan.service.validator.*;
import wisniewski.jan.service.exception.AdminServiceException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AdminService {

    private final CinemaRepository cinemaRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    private final SeanceRepository seanceRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final SeatsSeancesRepository seatsSeancesRepository;
    private final CityRepository cityRepository;
    private final TicketRepository ticketRepository;

    private final SeatService seatService;

    public Optional<Seance> editSeance(Seance seance) {
        return seanceRepository.update(seance);
    }

    public Optional<SeatsSeance> editSeatSeance(SeatsSeance seatsSeance) {
        return seatsSeancesRepository.update(seatsSeance);
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

    public Optional<CinemaRoom> editCinemaRoom(CinemaRoom cinemaRoom) {
        return cinemaRoomRepository.update(cinemaRoom);
    }

    public Optional<Movie> editMovie(Movie movie) {
        return movieRepository.update(movie);
    }

    public Optional<City> editCity(City city) {
        return cityRepository.update(city);
    }

    public Optional<Cinema> editCinema(Cinema cinema) {
        return cinemaRepository.update(cinema);
    }

    public Integer addCinemaRoom(CreateCinemaRoomDto cinemaRoomDto) {
        if (Objects.isNull(cinemaRoomDto)) {
            throw new AdminServiceException("CinemaRoomDto is null");
        }

        var validator = new CreateCinemaRoomDtoValidator();
        var errors = validator.validate(cinemaRoomDto);

        if (!errors.isEmpty()) {
            String errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add cinema room errors: " + errorsMessage);
        }

        if (cinemaRoomRepository.findByNameAndCinemaId(cinemaRoomDto.getName(), cinemaRoomDto.getCinemaId()).isPresent()) {
            throw new AdminServiceException("Cinema room with this name is already on this cinema id");
        }

        var cinemaRoomToAdd = Mapper.fromCinemaRoomDtoToCinemaRoom(cinemaRoomDto);

        var addedCinemaRoom = cinemaRoomRepository
                .add(cinemaRoomToAdd)
                .orElseThrow(() -> new AdminServiceException("Cannot insert to db"));

        if (seatService.addPlacesToNewRows(addedCinemaRoom, 1, addedCinemaRoom.getRowsNumber()).size() == 0) {
            throw new AdminServiceException("Failed to create seats for cinemaRoom!");
        }

        return addedCinemaRoom.getId();
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

        if (seanceRepository.isUniqueSeance(seanceDto).isPresent()) {
            throw new AdminServiceException("Seance is not unique. The same movieId, cinemaRoomId i LocalDate");
        }

        if (!seanceRepository.isMovieDisplayed(seanceDto)) {
            throw new AdminServiceException("Screening date is not within the time frame of the movie");
        }

        var seance = Mapper.fromSeanceDtoToSeance(seanceDto);

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

    public Integer addMovie(CreateMovieDto movieDto) {
        if (movieDto == null) {
            throw new AdminServiceException("movieDto is null");
        }
        var validator = new CreateMovieDtoValidator();
        var errors = validator.validate(movieDto);
        if (!errors.isEmpty()) {
            String errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add movie errors: " + errorsMessage);
        }

        if (movieRepository.isUniqueMovie(movieDto).isPresent()) {
            throw new AdminServiceException("This movie with that dateFrom and dateTo is already on db");
        }

        var movieToAdd = Mapper.fromMovieDtoToMovie(movieDto);
        var addedMovie = movieRepository
                .add(movieToAdd)
                .orElseThrow(() -> new AdminServiceException("cannot insert to db"));
        return addedMovie.getId();
    }

    public Integer deleteCinemaRoom(CinemaRoom cinemaRoom) {
        if (!seanceRepository.findFutureSeancesAtCinemaRoom(cinemaRoom.getId()).isEmpty()) {
            System.out.println("Can't delete this cinema room! At this cinema room movie will be displayed");
            return 0;
        }
        return (cinemaRoomRepository.deleteById(cinemaRoom.getId())) ? 1 : 0;
    }

    public Integer deleteCity(City city) {
        if (!seanceRepository.findByCity(city).isEmpty()) {
            System.out.println("Can't delete city! At cinema in this city movie will be displayed");
            return 0;
        }
        return (cityRepository.deleteById(city.getId())) ? 1 : 0;
    }

    public Integer deleteSeance(Seance seance) {
        if (!ticketRepository.findBySeance(seance).isEmpty()) {
            System.out.println("Can't delete seance! Ticket sales have already started");
            return 0;
        }
        return (seanceRepository.deleteById(seance.getId())) ? 1 : 0;
    }

    public Integer deleteMovie(Movie movie) {
        if (!seanceRepository.findByMovie(movie).isEmpty()) {
            System.out.println("Can't delete movie! Movie will be displayed!");
            return 0;
        }
        return (movieRepository.deleteById(movie.getId())) ? 1 : 0;
    }

    public Integer deleteCinema(Cinema cinema) {
        if (!seanceRepository.findByCinema(cinema).isEmpty()) {
            System.out.println("Can't delete cinema! Seances will be displayed there!");
            return 0;
        }
        System.out.println("Deleted cinemas rooms associated with this cinema: " +cinemaRoomRepository.deleteAllByCinemaId(cinema));
        return (cinemaRepository.deleteById(cinema.getId())) ? 1 : 0;
    }

    public Integer addCity(CreateCityDto cityDto) {
        var validator = new CreateCityDtoValidator();
        var errors = validator.validate(cityDto);
        if (!errors.isEmpty()) {
            String errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add city errors: " + errorsMessage);
        }

        if (cityRepository.findByName(cityDto.getName()).isPresent()) {
            throw new AdminServiceException("This city already exists in database");
        }

        City cityToAdd = City
                .builder()
                .name(cityDto.getName())
                .build();

        cityRepository.add(cityToAdd);
        return cityRepository.findLast().orElseThrow(() -> new AdminServiceException("Failed")).getId();
    }
}