package wisniewski.jan.ui;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.dto.*;
import wisniewski.jan.persistence.enums.SearchCriterion;
import wisniewski.jan.persistence.enums.SeatState;
import wisniewski.jan.persistence.mappers.Mapper;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.service.AdminService;
import wisniewski.jan.service.TicketService;
import wisniewski.jan.ui.exceptions.MenuServiceException;
import wisniewski.jan.ui.user_data.UserDataService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MenuService {

    private CinemaRepository cinemaRepository;
    private CinemaRoomRepository cinemaRoomRepository;
    private SeanceRepository seanceRepository;
    private MovieRepository movieRepository;
    private SeatsSeancesRepository seatsSeancesRepository;
    private SeatRepository seatRepository;
    private CityRepository cityRepository;
    private UserRepository userRepository;
    private TicketService ticketService;
    private ReservationRepository reservationRepository;
    private AdminService adminService;

    public MenuService(CinemaRepository cinemaRepository, CinemaRoomRepository cinemaRoomRepository,
                       SeanceRepository seanceRepository, MovieRepository movieRepository,
                       SeatsSeancesRepository seatsSeancesRepository, SeatRepository seatRepository,
                       TicketService ticketService, AdminService adminService,
                       CityRepository cityRepository, ReservationRepository reservationRepository,
                       UserRepository userRepository
    ) {
        this.cinemaRepository = cinemaRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.seanceRepository = seanceRepository;
        this.movieRepository = movieRepository;
        this.seatsSeancesRepository = seatsSeancesRepository;
        this.seatRepository = seatRepository;
        this.ticketService = ticketService;
        this.adminService = adminService;
        this.cityRepository = cityRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public void mainMenu() {
        while (true) {
            try {
                System.out.println("100 - [ADMIN] Add Movie");
                System.out.println("101 - [ADMIN] Add Cinema");
                System.out.println("102 - [ADMIN] Add Cinema Room");
                System.out.println("103 - [ADMIN] Add Seance");
                System.out.println("104 - [ADMIN] Add City");
                System.out.println("2 - Buy Ticket");
                System.out.println("3 - Buy Reserved Ticket");
                int decision = UserDataService.getInteger("Type option");
                switch (decision) {
                    case 2 -> option1();
                    case 3 -> option6();
                    case 100 -> option2();
                    case 101 -> option3();
                    case 102 -> option4();
                    case 103 -> option5();
                    case 104 -> option7();
                    default -> System.out.println("No option with this number");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MenuServiceException("Failed");
            }
        }
    }

    private void option7() {
        System.out.println("___ Add City ___");
        var cityToAdd = CreateCityDto
                .builder()
                .name(UserDataService.getString("Type city name"))
                .build();
        adminService.addCity(cityToAdd);
    }

    private void option6() {
        System.out.println("___ Buy Reserved Ticket ___");
        String email = UserDataService.getString("Type email");
        if (reservationRepository.findByEmail(email).isEmpty()) {
            System.out.println("No reservation for this email");
            return;
        }
        System.out.println("___ Reservations for: " + email + " ___");
        AtomicInteger counter = new AtomicInteger();
        List<ReservationWithUser> userReservationList = reservationRepository.findByEmail(email);
        String reservations = reservationRepository
                .findByEmail(email)
                .stream()
                .map(reservation -> {
                    Seance seance = seanceRepository.findById(reservation.getSeanceId())
                            .orElseThrow(() -> new MenuServiceException("Failed"));
                    String movieTitle = movieRepository.findById(seance.getMovieId()).orElseThrow(() -> new MenuServiceException("Failed")).getTitle();
                    CinemaRoom cinemaRoomName = cinemaRoomRepository.findById(seance.getCinemaRoomId()).orElseThrow(() -> new MenuServiceException("Failed"));
                    String cinemaName = cinemaRepository.findById(cinemaRoomName.getCinemaId()).orElseThrow(() -> new MenuServiceException("Failed")).getName();
                    LocalDateTime date = seance.getDateTime();
                    Seat seat = seatRepository.findById(reservation.getSeatId()).orElseThrow(() -> new MenuServiceException("Failed"));
                    int rowNumber = seat.getRowsNumber();
                    int placeNumber = seat.getPlace();
                    return new StringBuilder()
                            .append("-----" + counter.incrementAndGet() + "-----\n")
                            .append("Movie: " + movieTitle + "\n")
                            .append("Cinema: " + cinemaName + "\n")
                            .append("Room: " + cinemaRoomName.getName() + "\n")
                            .append("Date: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n")
                            .append("Row/place: " + rowNumber + "/" + placeNumber + "\n")
                            .toString();
                })
                .collect(Collectors.joining("\n"));
        System.out.println(reservations);
        int reservation;
        do {
            reservation = UserDataService.getInteger("Choose your reservation");
        } while (reservation < 1 || reservation > userReservationList.size());
        Reservation seanceReservation = reservationRepository.findById(
                userReservationList.get(reservation - 1).getReservationId()
        ).orElseThrow(() -> new MenuServiceException("Failed2"));
        SeatsSeance seatsSeance = seatsSeancesRepository.findBySeatId(seanceReservation.getSeatId()).orElseThrow(() -> new MenuServiceException("Failed2"));
        buyTickets(seanceReservation.getSeanceId(), seatsSeance);
        reservationRepository.deleteById(seanceReservation.getId());
    }

    private void option5() {
        System.out.println("___ Add Seance ___");
        var seance = CreateSeanceDto
                .builder()
                .cinemaRoomId(UserDataService.getCinemaRoom("Type cinema room id", cinemaRoomRepository).getId())
                .movieId(UserDataService.getMovie("Type movie id", movieRepository).getId())
                .dateTime(UserDataService.getLocalDateTime())
                .build();
        System.out.println(adminService.addSeance(seance));
    }

    private void option4() {
        System.out.println("___ Add Cinema Rooms ___");
        var cinemaRoom = CreateCinemaRoomDto
                .builder()
                .name(UserDataService.getString("Type cinema room name"))
                .rows(UserDataService.getInteger("Type cinema room rows"))
                .places(UserDataService.getInteger("Type cinema room places"))
                .cinemaId(UserDataService.getCinema("Type cinema id", cinemaRepository).getId())
                .build();
        System.out.println(adminService.addCinemaRoom(cinemaRoom));
    }

    private void option3() {
        System.out.println("___ Add Cinema ___");
        var cinema = CreateCinemaDto
                .builder()
                .cityId(UserDataService.getCity("Type city id:", cityRepository).getId())
                .name(UserDataService.getString("Type cinema name"))
                .build();
        System.out.println(adminService.addCinema(cinema));
    }

    private void option2() {
        System.out.println("___ Add Movie ___");
        var movie = CreateMovieDto
                .builder()
                .title(UserDataService.getString("Type movie title:"))
                .genre(UserDataService.getMovieGenre("Type movie genre"))
                .dateFrom(UserDataService.getLocalDateTime())
                .dateTo(UserDataService.getLocalDateTime())
                .build();
        System.out.println(adminService.addMovie(movie));
    }

    private void option1() {
        System.out.println("___ Buy / Reserve Ticket ___");
        SearchCriterion searchCriterion = UserDataService.getSearchCriterion("Choose filter");
        int cinemaId;
        List<CinemaRoom> cinemaRooms;
        switch (searchCriterion) {
            case CINEMA -> {
                do {
                    String cinemas = cinemaRepository
                            .findAll()
                            .stream()
                            .map(cinema -> cinema.getId() + " : " + cinema.getName())
                            .collect(Collectors.joining("\n"));
                    if (cinemas.equals("")) {
                        System.out.println("No cinemas in database!");
                        return;
                    }
                    System.out.println(cinemas);
                    cinemaId = UserDataService.getInteger("Type cinema id");
                }
                while (cinemaRepository.findById(cinemaId).isEmpty());
                cinemaRooms = cinemaRoomRepository.findByCinemaId(cinemaId);
                showSeances(cinemaRooms);
            }
            case CITY -> {
            }
            case SEANCE -> {
            }
        }
    }

    private void showSeances(List<CinemaRoom> cinemaRooms) {
        System.out.println("Available seances at: " + cinemaRepository
                .findById(cinemaRooms.get(0).getCinemaId())
                .orElseThrow(() -> new MenuServiceException("Failed!"))
                .getName()
        );
        Integer seanceId;
        do {
            seanceRepository.findSeancesByCinemaRooms(cinemaRooms)
                    .stream()
                    .map(seance -> seance.getId() + " : " +
                            movieRepository
                                    .findById(seance.getMovieId())
                                    .orElseThrow(() -> new MenuServiceException("Movie id not found"))
                                    .getTitle()
                    )
                    .forEach(System.out::println);
            seanceId = UserDataService.getInteger("Type seance id");
        } while (seanceRepository.findById(seanceId).isEmpty());
        chooseCinemaRoomPlace(seanceId);
    }

    private void chooseCinemaRoomPlace(Integer seanceId) {
        Optional<Seance> chosenSeance = seanceRepository.findById(seanceId);
        int chosenCinemaRoomId = chosenSeance
                .orElseThrow(() -> new MenuServiceException("FAILED"))
                .getCinemaRoomId();
        showCinemaRoomPlaces(seanceId);
        int rowNumber;
        int placeNumber;
        do {
            rowNumber = UserDataService.getInteger("Type row number");
            placeNumber = UserDataService.getInteger("Type place number at " + rowNumber + " row");
        }
        while (seatRepository.findByRowAndPlaceAtCinemaRoom(rowNumber, placeNumber, chosenCinemaRoomId).isEmpty());
        System.out.println("PLACE: " + rowNumber + "/" + placeNumber);
        int chosenSeatId = seatRepository
                .findByRowAndPlaceAtCinemaRoom(rowNumber, placeNumber, chosenCinemaRoomId)
                .orElseThrow(() -> new MenuServiceException("Failed"))
                .getId();

        SeatsSeance chosenSeatSeance = seatsSeancesRepository
                .findBySeatId(chosenSeatId)
                .orElseThrow(() -> new MenuServiceException("Failed"));

        if (!chosenSeatSeance.getState().equals(SeatState.FREE)) {
            System.out.println("This place is not free. Can't buy or reserve ticket!");
            return;
        }
        int decision;
        do {
            decision = UserDataService.getInteger("1 - buy / 2 - reserve");
        } while (decision != 1 && decision != 2);
        if (decision == 1) {
            buyTickets(seanceId, chosenSeatSeance);
        } else {
            reserveTicket(seanceId, chosenSeatSeance);
        }
        seatsSeancesRepository.update(chosenSeatSeance);
    }

    private void reserveTicket(Integer seanceId, SeatsSeance chosenSeatSeance) {
        chosenSeatSeance.setState(SeatState.RESERVED);
        var reservationDto = CreateReservationDto
                .builder()
                .seanceId(seanceId)
                .seatId(chosenSeatSeance.getSeatId())
                .userId(1)
                .build();
        var reservation = Mapper.fromCreateReservationDtoToReservation(reservationDto);
        reservationRepository.add(reservation);
        System.out.println("Ticket reserved!");
    }

    private void buyTickets(Integer seanceId, SeatsSeance chosenSeatSeance) {
        System.out.println("Buying tickets");
        BigDecimal ticketPrice = new BigDecimal(new Random().nextInt(100) + 10);
        int ticketDecision;
        var ticket = CreateTicketDto
                .builder()
                .seanceId(seanceId)
                .seatId(chosenSeatSeance.getSeatId())
                .price(ticketPrice)
                .discount(BigDecimal.ZERO)
                .userId(1)
                .build();
        do {
            System.out.println("Buy ticket for: " + ticketPrice + "?");
            ticketDecision = UserDataService.getInteger("1 - accept / 2 - denied");
        } while (ticketDecision != 1 && ticketDecision != 2);
        switch (ticketDecision) {
            case 1 -> {
                ticketService.buyTicket(ticket);
                chosenSeatSeance.setState(SeatState.ORDERED);
                seatsSeancesRepository.update(chosenSeatSeance);
            }
            case 2 -> {
                return;
            }
            default -> System.out.println("Wrong option");
        }

    }

    private void showCinemaRoomPlaces(Integer seanceId) {
        int cinemaRoomId = seanceRepository
                .findById(seanceId)
                .orElseThrow(() -> new MenuServiceException("Failed!"))
                .getCinemaRoomId();
        int placesAtCinemaRoom = cinemaRoomRepository
                .findById(cinemaRoomId)
                .orElseThrow(() -> new MenuServiceException("Failed 2!"))
                .getRowsNumber();
        List<SeatsSeance> seatsSeances = seatsSeancesRepository.findBySeanceId(seanceId);
        int seatId = 0;
        for (int i = 1; i <= placesAtCinemaRoom; i++) {
            for (int j = 1; j <= placesAtCinemaRoom; j++) {
                System.out.print(seatsSeances
                        .get(seatId)
                        .getState()
                        .name()
                        .charAt(0));
                seatId++;
            }
            System.out.println();
        }
    }
}
