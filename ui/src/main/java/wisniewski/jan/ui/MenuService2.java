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
import wisniewski.jan.ui.user_data.UserDataService2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MenuService2 {

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

    public MenuService2(CinemaRepository cinemaRepository, CinemaRoomRepository cinemaRoomRepository,
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
                System.out.println("-------[Movie]-------\nADD (100) | EDIT (107)");
                System.out.println("-------[Cinema]-------\nADD (101) | EDIT (105)");
                System.out.println("-------[Cinema Room]-------\nADD (102) | EDIT (108)");
                System.out.println("-------[Seance]-------\nADD (103) | EDIT (109)");
                System.out.println("-------[City]-------\nADD (104) | EDIT (106)");
                System.out.println("-----");
                System.out.println("2 - Buy Ticket");
                System.out.println("3 - Buy Reserved Ticket");
                int decision = UserDataService2.getInteger("Type option");
                switch (decision) {
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

    private void option12() {
        System.out.println("___ Edit Seance ___");
        Seance seance = UserDataService2.getSeance("Type seance to edit", seanceRepository, movieRepository);
        if (UserDataService2.getBoolean("Edit movie? (" + movieRepository.findById(seance.getMovieId()).orElseThrow(() -> new MenuServiceException("Failed")).getTitle() + ")")) {
            seance.setMovieId(UserDataService2.getInteger("Type new movie id"));
        }
        if (UserDataService2.getBoolean("Edit cinema room? (" + cinemaRoomRepository.findById(seance.getCinemaRoomId()).orElseThrow(() -> new MenuServiceException("Failed")).getName() + ")")) {
            seance.setMovieId(UserDataService2.getInteger("Type new movie id"));
        }
        if (UserDataService2.getBoolean("Edit date time? (" + seance.getDateTime() + ")")) {
            seance.setDateTime(UserDataService2.getLocalDateTime());
        }
        seanceRepository.update(seance);
    }

    private void option11() {
        System.out.println("___ Edit Cinema Room ___");
        CinemaRoom cinemaRoom = UserDataService2.getCinemaRoom("Type cinema room to edit", cinemaRoomRepository);
        if (UserDataService2.getBoolean("Edit name? (" + cinemaRoom.getName() + ")")) {
            cinemaRoom.setName(UserDataService2.getString("Type new name"));
        }
        if (UserDataService2.getBoolean("Edit cinema? (" + cinemaRepository.findById(cinemaRoom.getCinemaId()).orElseThrow(() -> new MenuServiceException("Failed")).getName() + ")")) {
            cinemaRoom.setCinemaId(UserDataService2.getCinema("Type new cinema id", cinemaRepository).getId());
        }
        if (UserDataService2.getBoolean("Edit rows? (" + cinemaRoom.getRowsNumber() + ")")) {
            cinemaRoom.setRowsNumber(UserDataService2.getInteger("Type new rows number"));
        }
        if (UserDataService2.getBoolean("Edit places in row? (" + cinemaRoom.getPlaces() + ")")) {
            cinemaRoom.setPlaces(UserDataService2.getInteger("Type new places number at row"));
        }

        //pobranie miejsc do usuniecia
        if (cinemaRoomRepository.findById(cinemaRoom.getId()).orElseThrow(() -> new MenuServiceException("Failed")).getRowsNumber() > cinemaRoom.getRowsNumber()) {
            System.out.println("wykrylem, ze usunales jakies rzedy");

        }

        //czy na te miejsca nie są zarezerwowane?

        cinemaRoomRepository.update(cinemaRoom);
    }

    private void option10() {
        System.out.println("___ Edit Movie ___");
        Movie chosenMovie = UserDataService2.getMovie("Type movie to edit", movieRepository);
        if (UserDataService2.getBoolean("Edit title? (" + chosenMovie.getTitle() + ")")) {
            chosenMovie.setTitle(UserDataService2.getString("Type new title"));
        }
        if (UserDataService2.getBoolean("Edit genre? (" + chosenMovie.getGenre().name() + ")")) {
            chosenMovie.setGenre(UserDataService2.getMovieGenre("Type new genre id"));
        }
        if (UserDataService2.getBoolean("Edit date from? (" + chosenMovie.getDateFrom() + ")")) {
            chosenMovie.setDateFrom(UserDataService2.getLocalDateTime());
        }
        if (UserDataService2.getBoolean("Edit date to? (" + chosenMovie.getDateFrom() + ")")) {
            chosenMovie.setDateFrom(UserDataService2.getLocalDateTime());
        }
        movieRepository.update(chosenMovie);
    }

    private void option9() {
        System.out.println("___ Edit City ___");
        City chosenCity = UserDataService2.getCity("Type city to edit", cityRepository);
        if (UserDataService2.getBoolean("Edit name? (" + chosenCity.getName() + ")")) {
            chosenCity.setName(UserDataService2.getString("Type new name"));
        }
        cityRepository.update(chosenCity);
    }

    private void option8() {
        System.out.println("___ Edit Cinema ___");
        Cinema chosenCinema = UserDataService2.getCinema("Type cinema to edit", cinemaRepository);
        if (UserDataService2.getBoolean("Edit name? (" + chosenCinema.getName() + ")")) {
            chosenCinema.setName(UserDataService2.getString("Type new name"));
        }
        if (UserDataService2.getBoolean("Edit city? (" + cityRepository.findById(chosenCinema.getCityId()).orElseThrow(() -> new MenuServiceException("Failed")).getName() + ")")) {
            chosenCinema.setCityId(UserDataService2.getCity("Type city id", cityRepository).getId());
        }
        cinemaRepository.update(chosenCinema);
    }

    private void option7() {
        System.out.println("___ Add City ___");
        var cityToAdd = CreateCityDto
                .builder()
                .name(UserDataService2.getString("Type city name"))
                .build();
        adminService.addCity(cityToAdd);
    }

    private void option6() {
        System.out.println("___ Buy Reserved Ticket ___");
        String email = UserDataService2.getString("Type email");
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
            reservation = UserDataService2.getInteger("Choose your reservation");
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
                .cinemaRoomId(UserDataService2.getCinemaRoom("Type cinema room id", cinemaRoomRepository).getId())
                .movieId(UserDataService2.getMovie("Type movie id", movieRepository).getId())
                .dateTime(UserDataService2.getLocalDateTime())
                .build();
        System.out.println(adminService.addSeance(seance));
    }

    private void option4() {
        System.out.println("___ Add Cinema Rooms ___");
        var cinemaRoom = CreateCinemaRoomDto
                .builder()
                .name(UserDataService2.getString("Type cinema room name"))
                .rows(UserDataService2.getInteger("Type cinema room rows"))
                .places(UserDataService2.getInteger("Type cinema room places"))
                .cinemaId(UserDataService2.getCinema("Type cinema id", cinemaRepository).getId())
                .build();
        System.out.println(adminService.addCinemaRoom(cinemaRoom));
    }

    private void option3() {
        System.out.println("___ Add Cinema ___");
        var cinema = CreateCinemaDto
                .builder()
                .cityId(UserDataService2.getCity("Type city id:", cityRepository).getId())
                .name(UserDataService2.getString("Type cinema name"))
                .build();
        System.out.println(adminService.addCinema(cinema));
    }

    private void option2() {
        System.out.println("___ Add Movie ___");
        var movie = CreateMovieDto
                .builder()
                .title(UserDataService2.getString("Type movie title:"))
                .genre(UserDataService2.getMovieGenre("Type movie genre"))
                .dateFrom(UserDataService2.getLocalDateTime())
                .dateTo(UserDataService2.getLocalDateTime())
                .build();
        System.out.println(adminService.addMovie(movie));
    }

    private void option1() {
        System.out.println("___ Buy / Reserve Ticket ___");
        SearchCriterion searchCriterion = UserDataService2.getSearchCriterion("Choose filter");
        int cinemaId;
        int cityId;
        int seanceId;
        List<CinemaRoom> cinemaRooms;
        switch (searchCriterion) {
            case CINEMA -> {
                do {
                    cinemaId = UserDataService2.getCinema("Type cinema id", cinemaRepository).getId();
                }
                while (cinemaRepository.findById(cinemaId).isEmpty());
                cinemaRooms = cinemaRoomRepository.findByCinemaId(cinemaId);
                showSeances(cinemaRooms);
            }
            case CITY -> {
                do {
                    cityId = UserDataService2.getCity("Type city id", cityRepository).getId();
                } while (cityRepository.findById(cityId).isEmpty());
                System.out.println(cityId);
                cinemaRooms = cinemaRoomRepository.findByCityId(cityId);
                showSeances(cinemaRooms);
            }
            case SEANCE -> {
                do {
                    seanceId = UserDataService2.getSeance("Type seance id", seanceRepository, movieRepository).getId();
                } while (seanceRepository.findById(seanceId).isEmpty());
                
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
            seanceId = UserDataService2.getInteger("Type seance id");
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
            rowNumber = UserDataService2.getInteger("Type row number");
            placeNumber = UserDataService2.getInteger("Type place number at " + rowNumber + " row");
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
            decision = UserDataService2.getInteger("1 - buy / 2 - reserve");
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
            ticketDecision = UserDataService2.getInteger("1 - accept / 2 - denied");
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

        //////////////////

/*
        int rows = 5;
        int places = 5;


        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= places; j++) {

                if (i == 1 && j == 1) {
                    System.out.print(" O ");
                    continue;
                }
                System.out.print(" F ");
            }
            System.out.println();
        }
*/


        ////////////////////////

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
