package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.dto.*;
import wisniewski.jan.persistence.mappers.Mapper;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.persistence.validator.CreateCinemaDtoValidator;
import wisniewski.jan.persistence.validator.CreateCinemaRoomDtoValidator;
import wisniewski.jan.persistence.validator.CreateMovieDtoValidator;
import wisniewski.jan.persistence.validator.CreateSeanceDtoValidator;
import wisniewski.jan.service.exception.AdminServiceException;

import java.util.ArrayList;
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

    public Integer addCinema(CreateCinemaDto cinemaDto) {
        if (cinemaDto == null) {
            throw new AdminServiceException("Cinema dto is null");
        }
        var validator = new CreateCinemaDtoValidator();
        var errors = validator.validate(cinemaDto);
        if (validator.hasErrors()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add cinema errors:  " + errorsMessage);
        }

        //findByCinemaName -> jezeli jest to wyjątek, jezeli nie ma to idziemy dalej
        //1. createcineamdtonull
        //2. jak jest kino w db
        //3. prawidlowe wstawianie kina i sprawdzenie id

        if (cinemaRepository.findByName(cinemaDto.getName()).isPresent()) {
            throw new AdminServiceException("Cinema with this name is already on DB");
        }

        var cinema = Mapper.fromCinemaDtoToCinema(cinemaDto);
        var createdCinema = cinemaRepository
                .add(cinema)
                .orElseThrow(() -> new AdminServiceException("cannot insert to db"));
        return createdCinema.getId();
    }

    //------------------------------------------------[WIP]----------------------------------
    public Optional<CinemaRoom> editCinemaRoom(CinemaRoom cinemaRoom) {
        //List<Seat> cinemaRoomSeats = seatRepository.findAllByCinemaId(cinemaRoom);
        CinemaRoom cinemaRoomToEdit = cinemaRoomRepository
                .findById(cinemaRoom.getId())
                .orElseThrow(() -> new AdminServiceException("No cinema room with this id"));
        if (cinemaRoom.getRowsNumber() > cinemaRoomToEdit.getRowsNumber()) {
            //dodajemy rows
            int rowsToAdd = cinemaRoom.getRowsNumber() - cinemaRoomToEdit.getRowsNumber();
            generateSeats(rowsToAdd, cinemaRoomToEdit);
        }
        if (cinemaRoom.getRowsNumber() < cinemaRoomToEdit.getRowsNumber()) {
            //usuwamy rows
            //czy na ta miejsca nie ma rezerwacji? - walidacja
            int rowsToRemove = cinemaRoomToEdit.getRowsNumber() - cinemaRoom.getRowsNumber();
            seatRepository.removeAll(cinemaRoomToEdit, rowsToRemove);
        }
        return cinemaRoomRepository.update(cinemaRoom);
    }

    private List<Seat> generateSeats(int rowsToAdd, CinemaRoom cinemaRoom) {
        List<Seat> createdSeats = new ArrayList<>();
        for (int i = cinemaRoom.getRowsNumber() + 1; i < cinemaRoom.getRowsNumber() + rowsToAdd; i++) {
            for (int j = 1; j <= cinemaRoom.getPlaces(); j++) {
                createdSeats.add(Seat
                        .builder()
                        .cinemaRoomId(cinemaRoom.getId())
                        .place(j)
                        .rowsNumber(i)
                        .build());
            }
        }
        seatRepository.addAll(createdSeats);
        return createdSeats;
    }

    //------------------------------------------------[WIP]----------------------------------

    public Integer addCinemaRoom(CreateCinemaRoomDto cinemaRoomDto) {
        if (Objects.isNull(cinemaRoomDto)) {
            throw new AdminServiceException("CinemaRoomDto is null");
        }

        var validator = new CreateCinemaRoomDtoValidator();
        var errors = validator.validate(cinemaRoomDto);

        if (validator.hasErrors()) {
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

        if (generateCinemaRoomSeats(addedCinemaRoom) == 0) {
            throw new AdminServiceException("Failed to create seats for cinemaRoom!");
        }

        return addedCinemaRoom.getId();
    }

    private Integer generateCinemaRoomSeats(CinemaRoom cinemaRoom) {
        List<Seat> generatedSeats = new ArrayList<>();
        for (int i = 1; i <= cinemaRoom.getRowsNumber(); i++) {
            for (int j = 1; j <= cinemaRoom.getPlaces(); j++) {
                generatedSeats.add(
                        Seat
                                .builder()
                                .cinemaRoomId(cinemaRoom.getId())
                                .place(j)
                                .rowsNumber(i)
                                .build()
                );
            }
        }
        return seatRepository
                .addAll(generatedSeats);
    }

    public Integer addSeance(CreateSeanceDto seanceDto) {
        if (seanceDto == null) {
            throw new AdminServiceException("seance dto is null");
        }
        var validator = new CreateSeanceDtoValidator();
        var errors = validator.validate(seanceDto);
        if (validator.hasErrors()) {
            String errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add seance errors: " + errorsMessage);
        }

        if (seanceRepository.isUniqueSeance(seanceDto).isPresent()) {
            throw new AdminServiceException("seance is not unique. The same movieId, cinemaRoomId i LocalDate");
        }

        var seance = Mapper.fromSeanceDtoToSeance(seanceDto);

        var addedSeance = seanceRepository
                .add(seance)
                .orElseThrow(() -> new AdminServiceException("cannot insert to db"));

       getSeatsForCinemaRoomAndAddToSeance(addedSeance);

        return addedSeance.getId();
    }

    private Integer getSeatsForCinemaRoomAndAddToSeance(Seance seance) {
        int cinemaRoomId = seance.getCinemaRoomId();
        CinemaRoom cinemaRoom = cinemaRoomRepository
                .findById(cinemaRoomId)
                .orElseThrow(() -> new AdminServiceException("Failed"));

        List<Seat> seatsForCinemaRoom = seatRepository.findAllByCinemaId(cinemaRoom);
        return seatsSeancesRepository.addAll(seatsForCinemaRoom,seance);
    }

    public Integer addMovie(CreateMovieDto movieDto) {
        if (movieDto == null) {
            throw new AdminServiceException("movieDto is null");
        }
        var validator = new CreateMovieDtoValidator();
        var errors = validator.validate(movieDto);
        if (validator.hasErrors()) {
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

}