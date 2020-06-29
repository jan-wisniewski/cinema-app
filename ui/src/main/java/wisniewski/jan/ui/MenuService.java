package wisniewski.jan.ui;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.dto.*;
import wisniewski.jan.persistence.enums.SearchCriterion;
import wisniewski.jan.persistence.enums.SeatState;
import wisniewski.jan.persistence.mappers.Mapper;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.service.*;
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
    private ReservationRepository reservationRepository;

    private TicketService ticketService;
    private MovieService movieService;
    private AdminService adminService;
    private CinemaRoomService cinemaRoomService;
    private CityService cityService;
    private ReservationService reservationService;
    private SeanceService seanceService;
    private CinemaService cinemaService;
    private SeatService seatService;
    private SeatSeanceService seatSeanceService;

    public MenuService(CinemaRepository cinemaRepository, CinemaRoomRepository cinemaRoomRepository,
                       SeanceRepository seanceRepository, MovieRepository movieRepository,
                       SeatsSeancesRepository seatsSeancesRepository, SeatRepository seatRepository,
                       TicketService ticketService, AdminService adminService,
                       CityRepository cityRepository, ReservationRepository reservationRepository,
                       MovieService movieService, CinemaRoomService cinemaRoomService,
                       CityService cityService, ReservationService reservationService,
                       SeanceService seanceService, CinemaService cinemaService,
                       SeatService seatService, SeatSeanceService seatSeanceService
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
        this.movieService = movieService;
        this.cinemaRoomService = cinemaRoomService;
        this.cityService = cityService;
        this.seanceService = seanceService;
        this.reservationService = reservationService;
        this.cinemaService = cinemaService;
        this.seatService = seatService;
        this.seatSeanceService = seatSeanceService;
    }

    public void mainMenu() {
        while (true) {
            try {
                System.out.println("\n___ CINEMA MENU ___");
                System.out.println("-------[Movie]-------\nADD (100) | EDIT (107)");
                System.out.println("-------[Cinema]-------\nADD (101) | EDIT (105)");
                System.out.println("-------[Cinema Room]-------\nADD (102) | EDIT (108)");
                System.out.println("-------[Seance]-------\nADD (103) | EDIT (109)");
                System.out.println("-------[City]-------\nADD (104) | EDIT (106)");
                System.out.println("-------[Tickets]-------\nBUY (2) | RESERVATIONS (3)");
                System.out.println("-------[Others]-------\nEXIT (0) | HELP (1)\n");
                int decision = UserDataService.getInteger("___ Type option ___");
                switch (decision) {
                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    case 1 -> option13();
                    case 2 -> option1();
                    case 3 -> option6();
                    case 100 -> option2();
                    case 101 -> option3();
                    case 102 -> option4();
                    case 103 -> option5();
                    case 104 -> option7();
                    case 105 -> option8();
                    case 106 -> option9();
                    case 107 -> option10();
                    case 108 -> option11();
                    case 109 -> option12();
                    default -> System.out.println("No option with this number");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MenuServiceException("Failed");
            }
        }
    }

    private void option13() {
        System.out.println("___ HELP ___");
        System.out.println("1. Tutaj jakiś poradnik");
    }

    private void option12() {
        System.out.println("___ Edit Seance ___");
        //TODO REPO DO WYWALENIA
        Seance seance = UserDataService.getSeance("Type seance to edit", seanceRepository, movieRepository);
        if (UserDataService.getBoolean("Edit movie? (" + movieService.showTitle(seance.getMovieId()) + ")")) {
            seance.setMovieId(UserDataService.getInteger("Type new movie id"));
        }
        if (UserDataService.getBoolean("Edit cinema room? (" + cinemaRoomService.getName(seance.getCinemaRoomId()) + ")")) {
            seance.setMovieId(UserDataService.getInteger("Type new movie id"));
        }
        if (UserDataService.getBoolean("Edit date time? (" + seance.getDateTime() + ")")) {
            seance.setDateTime(UserDataService.getLocalDateTime());
        }
        System.out.println(adminService.editSeance(seance));
    }

    private void option11() {
        System.out.println("___ Edit Cinema Room ___");
        //TODO REPO DO WYWALENIA
        CinemaRoom cinemaRoom = UserDataService.getCinemaRoom("Type cinema room to edit", cinemaRoomRepository);
        if (UserDataService.getBoolean("Edit name? (" + cinemaRoom.getName() + ")")) {
            cinemaRoom.setName(UserDataService.getString("Type new name"));
        }
        if (UserDataService.getBoolean("Edit cinema? (" + cinemaRoomService.getName(cinemaRoom.getId()) + ")")) {
            cinemaRoom.setCinemaId(UserDataService.getCinema("Type new cinema id", cinemaRepository).getId());
        }
        if (UserDataService.getBoolean("Edit rows? (" + cinemaRoom.getRowsNumber() + ")")) {
            cinemaRoom.setRowsNumber(UserDataService.getInteger("Type new rows number"));
        }
        if (UserDataService.getBoolean("Edit places in row? (" + cinemaRoom.getPlaces() + ")")) {
            cinemaRoom.setPlaces(UserDataService.getInteger("Type new places number at row"));
        }

        //TODO
        //pobranie miejsc do usuniecia
        if (cinemaRoomService.getRowsNumber(cinemaRoom.getId()) > cinemaRoom.getRowsNumber()) {
            System.out.println("wykrylem, ze usunales jakies rzedy");
        }

        //czy na te miejsca nie są zarezerwowane?

        System.out.println(adminService.editCinemaRoom(cinemaRoom));

    }

    private void option10() {
        System.out.println("___ Edit Movie ___");
        //TODO REPO DO WYWALENIA
        Movie movie = UserDataService.getMovie("Type movie to edit", movieRepository);
        if (UserDataService.getBoolean("Edit title? (" + movie.getTitle() + ")")) {
            movie.setTitle(UserDataService.getString("Type new title"));
        }
        if (UserDataService.getBoolean("Edit genre? (" + movie.getGenre().name() + ")")) {
            movie.setGenre(UserDataService.getMovieGenre("Type new genre id"));
        }
        if (UserDataService.getBoolean("Edit date from? (" + movie.getDateFrom() + ")")) {
            movie.setDateFrom(UserDataService.getLocalDateTime());
        }
        if (UserDataService.getBoolean("Edit date to? (" + movie.getDateFrom() + ")")) {
            movie.setDateFrom(UserDataService.getLocalDateTime());
        }
        System.out.println(adminService.editMovie(movie));
    }

    private void option9() {
        System.out.println("___ Edit City ___");
        //TODO REPO DO WYWALENIA
        City city = UserDataService.getCity("Type city to edit", cityRepository);
        if (UserDataService.getBoolean("Edit name? (" + city.getName() + ")")) {
            city.setName(UserDataService.getString("Type new name"));
        }
        System.out.println(adminService.editCity(city));
    }

    private void option8() {
        System.out.println("___ Edit Cinema ___");
        //TODO REPO DO WYWALENIA
        Cinema cinema = UserDataService.getCinema("Type cinema to edit", cinemaRepository);
        if (UserDataService.getBoolean("Edit name? (" + cinema.getName() + ")")) {
            cinema.setName(UserDataService.getString("Type new name"));
        }
        if (UserDataService.getBoolean("Edit city? (" + cityService.showName(cinema.getCityId()) + ")")) {
            cinema.setCityId(UserDataService.getCity("Type city id", cityRepository).getId());
        }
        System.out.println(adminService.editCinema(cinema));
    }

    private void option7() {
        System.out.println("___ Add City ___");
        var cityToAdd = CreateCityDto
                .builder()
                .name(UserDataService.getString("Type city name"))
                .build();
        System.out.println(adminService.addCity(cityToAdd));
    }

    private void option6() {
        System.out.println("___ Buy Reserved Ticket ___");
        String email = UserDataService.getString("Type email");
        if (reservationService.showReservations(email).isEmpty()) {
            System.out.println("No reservation for this email");
            return;
        }
        System.out.println("___ Reservations for: " + email + " ___");
        AtomicInteger counter = new AtomicInteger();
        List<ReservationWithUser> userReservationList = reservationService.showReservations(email);
        String reservations = userReservationList
                .stream()
                .map(reservation -> {
                    Seance seance = seanceService.getSeance(reservation.getSeanceId());
                    String movieTitle = movieService.showTitle(seance.getMovieId());
                    CinemaRoom cinemaRoomName = cinemaRoomService.getCinemaRoom(seance.getCinemaRoomId());
                    String cinemaName = cinemaService.getName(cinemaRoomName.getCinemaId());
                    LocalDateTime date = seance.getDateTime();
                    Seat seat = seatService.getSeat(reservation.getSeatId());
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

        Reservation seanceReservation = reservationService.getReservation(userReservationList.get(reservation - 1).getReservationId());
        SeatsSeance seatsSeance = seatSeanceService.getSeatSeances(seanceReservation.getSeatId());
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
        adminService.addSeance(seance);
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
        adminService.addCinema(cinema);
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
        int cityId;
        int seanceId;
        List<CinemaRoom> cinemaRooms;
        switch (searchCriterion) {
            case CINEMA -> {
                do {
                    cinemaId = UserDataService.getCinema("Type cinema id", cinemaRepository).getId();
                }
                while (cinemaRepository.findById(cinemaId).isEmpty());
                cinemaRooms = cinemaRoomService.getCinemaRoomList(cinemaId);
                showSeances(cinemaRooms);
            }
            case CITY -> {
/*                do {
                    cityId = UserDataService.getCity("Type city id", cityRepository).getId();
                } while (cityRepository.findById(cityId).isEmpty());
                System.out.println(cityId);
                cinemaRooms = cinemaRoomRepository.findByCityId(cityId);
                showSeances(cinemaRooms);*/
            }
            case SEANCE -> {
/*
                do {
                    seanceId = UserDataService.getSeance("Type seance id", seanceRepository, movieRepository).getId();
                } while (seanceRepository.findById(seanceId).isEmpty());
*/

            }
        }
    }

    private void showSeances(List<CinemaRoom> cinemaRooms) {
        System.out.println("Available seances at: " + cinemaService.getName(cinemaRooms.get(0).getCinemaId()));
        Integer seanceId;
        do {
            seanceService.getSeancesList(cinemaRooms)
                    .stream()
                    .map(seance -> seance.getId() + " : " + movieService.showTitle(seance.getMovieId()))
                    .forEach(System.out::println);
            seanceId = UserDataService.getInteger("Type seance id");

            //TODO DO ZMIANY REPO
        } while (seanceRepository.findById(seanceId).isEmpty());
        chooseCinemaRoomPlace(seanceId);
    }

    private void chooseCinemaRoomPlace(Integer seanceId) {
        Seance seance = seanceService.getSeance(seanceId);
        int cinemaRoomId = seance.getCinemaRoomId();
        showCinemaRoomPlaces(seanceId);
        int rowNumber;
        int placeNumber;
        do {
            rowNumber = UserDataService.getInteger("Type row number");
            placeNumber = UserDataService.getInteger("Type place number at " + rowNumber + " row");
        }
        while (seatRepository.findByRowAndPlaceAtCinemaRoom(rowNumber, placeNumber, cinemaRoomId).isEmpty());
        System.out.println("PLACE: " + rowNumber + "/" + placeNumber);
        int chosenSeatId = seatService.getSeatId(rowNumber, placeNumber, cinemaRoomId);

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

        int rowsCounter = 1;
        int placeCounter = 1;

        for (int i = 0; i <= placesAtCinemaRoom; i++) {
            for (int j = 0; j <= placesAtCinemaRoom; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("   ");
                    continue;
                }
                if (i == 0) {
                    System.out.print(" " + rowsCounter++ + " ");
                    continue;
                }
                if (j == 0) {
                    System.out.print(" " + placeCounter++ + " ");
                    continue;
                }
                System.out.print(" " + seatsSeances
                        .get(seatId)
                        .getState()
                        .name()
                        .charAt(0) + " ");
                seatId++;
            }
            System.out.println();
        }
    }
}
