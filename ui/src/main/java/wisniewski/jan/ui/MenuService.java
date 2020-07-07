package wisniewski.jan.ui;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.view.ReservationWithUser;
import wisniewski.jan.persistence.model.view.SeatsSeanceWithSeanceDate;
import wisniewski.jan.service.dto.*;
import wisniewski.jan.service.enums.SearchCriterion;
import wisniewski.jan.persistence.enums.SeatState;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.service.service.*;
import wisniewski.jan.ui.exceptions.MenuServiceException;
import wisniewski.jan.ui.user_data.UserDataService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MenuService {

    private final TicketService ticketService;
    private final MovieService movieService;
    private final AdminService adminService;
    private final CinemaRoomService cinemaRoomService;
    private final CityService cityService;
    private final ReservationService reservationService;
    private final SeanceService seanceService;
    private final CinemaService cinemaService;
    private final SeatService seatService;
    private final SeatSeanceService seatSeanceService;

    public void mainMenu() {
        while (true) {
            try {
                System.out.println("\n___ CINEMA MENU ___");
                System.out.println("-------[Movie]-------\nADD (100) | EDIT (107) | DELETE (110)");
                System.out.println("-------[Cinema]-------\nADD (101) | EDIT (105) | DELETE (111)");
                System.out.println("-------[Cinema Room]-------\nADD (102) | EDIT (108) | DELETE (114)");
                System.out.println("-------[Seance]-------\nADD (103) | EDIT (109) | DELETE (113)");
                System.out.println("-------[City]-------\nADD (104) | EDIT (106) | DELETE (112)");
                System.out.println("-------[Tickets]-------\nBUY (2) | RESERVATIONS (3)");
                System.out.println("-------[Others]-------\nEXIT (0) | STATISTICS (1)\n");
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
                    case 110 -> option14();
                    case 111 -> option15();
                    case 112 -> option16();
                    case 113 -> option17();
                    case 114 -> option18();
                    default -> System.out.println("No option with this number");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void option18() {
        System.out.println("___ Delete cinema room ___");
        if (cinemaRoomService.getAll().isEmpty()) {
            System.out.println("No cinemas rooms in database");
            return;
        }
        CinemaRoom cinemaRoom = UserDataService.getCinemaRoom("What cinema room do you want to delete?", cinemaRoomService);
        if (adminService.deleteCinemaRoom(cinemaRoom) > 0) {
            System.out.println("Cinema room " + cinemaRoom.getName() + " successfully deleted from db");
        }
    }

    private void option17() {
        System.out.println("___ Delete seance ___");
        if (seanceService.getAll().isEmpty()) {
            System.out.println("No seances in database");
            return;
        }
        Seance seance = UserDataService.getSeance("What seance do you want to delete?", seanceService);
        if (adminService.deleteSeance(seance) > 0) {
            System.out.println("Seance (id: " + seance.getId() + ") successfully deleted from db");
        }
    }

    private void option16() {
        System.out.println("___ Delete city ___");
        if (cityService.getAll().isEmpty()) {
            System.out.println("No cities in database!");
            return;
        }
        City city = UserDataService.getCity("What city do you want to delete?", cityService);
        if (adminService.deleteCity(city) > 0) {
            System.out.println("City " + city.getName() + " successfully deleted from db");
        }
    }

    private void option15() {
        System.out.println("___ Delete cinema ___");
        if (cinemaService.getAll().isEmpty()) {
            System.out.println("No cinemas in database!");
            return;
        }
        System.out.println("! All cinemas rooms associated with this cinema will also be deleted !");
        Cinema cinema = UserDataService.getCinema("What cinema do you want to delete?", cinemaService);
        if (adminService.deleteCinema(cinema) > 0) {
            System.out.println("Cinema " + cinema.getName() + " successfully deleted from db!");
        }
    }

    private void option14() {
        System.out.println("___ Delete movie ___");
        if (movieService.getAll().isEmpty()) {
            System.out.println("No movies in database!");
            return;
        }
        Movie chosenMovie = UserDataService.getMovie("What movie do you want to delete?", movieService);
        if (adminService.deleteMovie(chosenMovie) > 0) {
            System.out.println("Movie " + chosenMovie.getTitle() + " successfully deleted from db!");
        }
    }

    private void option13() {
        System.out.println("___ Statistics ___");
        System.out.println("1. Movies: " + movieService.getAll().size());
        System.out.println("2. Seances: " + seanceService.getAll().size());
        System.out.println("3. Cinemas: " + cinemaService.getAll().size());
        System.out.println("4. Cinemas Rooms: " + cinemaRoomService.getAll().size());
    }

    private void option12() {
        System.out.println("___ Edit Seance ___");
        if (seanceService.getAll().isEmpty()) {
            System.out.println("No seances in database");
            return;
        }
        Seance seance = UserDataService.getSeance("Type seance to edit", seanceService);
        if (UserDataService.getBoolean("Edit movie? (" + movieService.showTitle(seance.getMovieId()) + ")")) {
            seance.setMovieId(UserDataService.getInteger("Type new movie id"));
        }
        if (UserDataService.getBoolean("Edit cinema room? (" + cinemaRoomService.getNameByCinemaRoomId(seance.getCinemaRoomId()) + ")")) {
            seance.setMovieId(UserDataService.getInteger("Type new movie id"));
        }
        if (UserDataService.getBoolean("Edit date time? (" + seance.getDateTime() + ")")) {
            seance.setDateTime(UserDataService.getLocalDateTime("Type new date time"));
        }
        System.out.println(adminService.editSeance(seance));
    }

    private void option11() {
        System.out.println("___ Edit Cinema Room ___");
        if (cinemaRoomService.getAll().isEmpty()) {
            System.out.println("No cinemas rooms in database");
            return;
        }
        boolean editedPlaces;
        boolean editedRows;
        CinemaRoom cinemaRoom = UserDataService.getCinemaRoom("Type cinema room to edit", cinemaRoomService);
        if (UserDataService.getBoolean("Edit name? (" + cinemaRoom.getName() + ")")) {
            cinemaRoom.setName(UserDataService.getString("Type new name"));
        }
        if (UserDataService.getBoolean("Edit cinema? (" + cinemaService.getNameByCinemaId(cinemaRoom.getCinemaId()) + ")")) {
            cinemaRoom.setCinemaId(UserDataService.getCinema("Type new cinema id", cinemaService).getId());
        }
        if (UserDataService.getBoolean("Edit rows? (" + cinemaRoom.getRowsNumber() + ")")) {
            cinemaRoom.setRowsNumber(UserDataService.getInteger("Type new rows number"));
        }
        if (UserDataService.getBoolean("Edit places in row? (" + cinemaRoom.getPlaces() + ")")) {
            cinemaRoom.setPlaces(UserDataService.getInteger("Type new places number at row"));
        }

        //---------USUWANIE ROWS LUB PLACES Z CINEMA ROOM-----------

        editedRows = cinemaRoomService.getRowsNumberByCinemaRoomId(cinemaRoom.getId()) > cinemaRoom.getRowsNumber();
        editedPlaces = cinemaRoomService.getPlacesNumberInRowByCinemaRoomId(cinemaRoom.getId()) > cinemaRoom.getPlaces();
        List<Seat> seatToRemove;
        List<SeatsSeanceWithSeanceDate> reservedSeats;

        if (editedPlaces) {
            seatToRemove = seatService.findPlacesAboveSeatPlace(cinemaRoom, cinemaRoom.getRowsNumber());
            reservedSeats = seatSeanceService.isOneOfAPlaceReservedForFutureSeance(seatToRemove);
            if (seatSeanceService.isAvailableToRemoveSeats(seatToRemove)) {
                System.out.println("Can't remove places. One of them is reserved for future seance!");
                seatSeanceService.showReservedSeatsForFutureSeance(reservedSeats);
                return;
            } else {
                System.out.println(seatService.deleteSeats(seatToRemove));
            }
        }
        if (editedRows) {
            seatToRemove = seatService.findPlacesAboveRowNumber(cinemaRoom, cinemaRoom.getRowsNumber());
            reservedSeats = seatSeanceService.isOneOfAPlaceReservedForFutureSeance(seatToRemove);
            if (seatSeanceService.isAvailableToRemoveSeats(seatToRemove)) {
                System.out.println("Can't remove places. One of them is reserved for future seance!");
                seatSeanceService.showReservedSeatsForFutureSeance(reservedSeats);
                return;
            } else {
                System.out.println(seatService.deleteSeats(seatToRemove));
            }
        }

        //---------DODAWANIE ROWS LUB PLACES Z CINEMA ROOM-----------

        List<Seat> addedSeats = new ArrayList<>();

        editedRows = cinemaRoomService.getRowsNumberByCinemaRoomId(cinemaRoom.getId()) < cinemaRoom.getRowsNumber();
        editedPlaces = cinemaRoomService.getPlacesNumberInRowByCinemaRoomId(cinemaRoom.getId()) < cinemaRoom.getPlaces();

        if (editedRows && !editedPlaces) {
            System.out.println("Total number of seats after added new rows: "
                    + addedSeats.addAll(
                    seatService.addPlacesToNewRows(cinemaRoom, cinemaRoomService.getRowsNumberByCinemaRoomId(cinemaRoom.getId()) + 1, cinemaRoom.getRowsNumber() - cinemaRoomService.getRowsNumberByCinemaRoomId(cinemaRoom.getId()))));
        }

        if (editedPlaces && !editedRows) {
            System.out.println("Total number of seats after added new places: "
                    + addedSeats.addAll(
                    seatService.addPlacesToExistsRows(cinemaRoom)));
        }

        if (editedPlaces && editedRows) {
            System.out.println("Total number of seats after added new places and news rows: "
                    + addedSeats.addAll(
                    seatService.addNewPlacesAndRows(cinemaRoom)));
        }

        List<Integer> futureSeancesAtCinemaRoomIds = seanceService
                .findSeancesFromDateAtCinemaRoom(cinemaRoom.getId())
                .stream()
                .map(Seance::getId)
                .collect(Collectors.toList());
        System.out.println(addedSeats.size());

        futureSeancesAtCinemaRoomIds.forEach(fs -> {
            seatSeanceService.addAllBySeanceIds(addedSeats, fs);
        });

        adminService.editCinemaRoom(cinemaRoom);
    }

    private void option10() {
        System.out.println("___ Edit Movie ___");
        if (movieService.getAll().isEmpty()) {
            System.out.println("No movies on database!");
            return;
        }
        Movie movie = UserDataService.getMovie("Type movie to edit", movieService);
        if (UserDataService.getBoolean("Edit title? (" + movie.getTitle() + ")")) {
            movie.setTitle(UserDataService.getString("Type new title"));
        }
        if (UserDataService.getBoolean("Edit genre? (" + movie.getGenre().name() + ")")) {
            movie.setGenre(UserDataService.getMovieGenre("Type new genre id"));
        }
        if (UserDataService.getBoolean("Edit date from? (" + movie.getDateFrom() + ")")) {
            movie.setDateFrom(UserDataService.getLocalDateTime("Type new date from"));
        }
        if (UserDataService.getBoolean("Edit date to? (" + movie.getDateFrom() + ")")) {
            movie.setDateFrom(UserDataService.getLocalDateTime("Type new date to"));
        }
        System.out.println(adminService.editMovie(movie));
    }

    private void option9() {
        System.out.println("___ Edit City ___");
        if (cityService.getAll().isEmpty()) {
            System.out.println("No cities in database");
            return;
        }
        City city = UserDataService.getCity("Type city to edit", cityService);
        if (UserDataService.getBoolean("Edit name? (" + city.getName() + ")")) {
            city.setName(UserDataService.getString("Type new name"));
        }
        System.out.println(adminService.editCity(city));
    }

    private void option8() {
        System.out.println("___ Edit Cinema ___");
        if (cinemaService.getAll().isEmpty()) {
            System.out.println("No cinemas in database");
            return;
        }
        Cinema cinema = UserDataService.getCinema("Type cinema to edit", cinemaService);
        if (UserDataService.getBoolean("Edit name? (" + cinema.getName() + ")")) {
            cinema.setName(UserDataService.getString("Type new name"));
        }
        if (UserDataService.getBoolean("Edit city? (" + cityService.showNameByCityId(cinema.getCityId()) + ")")) {
            cinema.setCityId(UserDataService.getCity("Type city id", cityService).getId());
        }
        System.out.println(adminService.editCinema(cinema));
    }

    private void option7() {
        System.out.println("___ Add City ___");
        var cityToAdd = CreateCityDto
                .builder()
                .name(UserDataService.getString("Type city name"))
                .build();
        System.out.println(cityToAdd.getName() + " added with id: " + adminService.addCity(cityToAdd));
    }

    private void option6() {
        System.out.println("___ Buy Reserved Ticket ___");
        String email = UserDataService.getString("Type email");
        if (reservationService.showReservationsListByEmail(email).isEmpty()) {
            System.out.println("No reservation for this email");
            return;
        }
        System.out.println("___ Reservations for: " + email + " ___");
        AtomicInteger counter = new AtomicInteger();
        List<ReservationWithUser> userReservationList = reservationService.showReservationsListByEmail(email);
        String reservations = userReservationList
                .stream()
                .map(reservation -> {
                    Seance seance = seanceService.getSeanceById(reservation.getSeanceId()).orElseThrow(() -> new MenuServiceException("FAILED"));
                    String movieTitle = movieService.showTitle(seance.getMovieId());
                    CinemaRoom cinemaRoomName = cinemaRoomService.getCinemaRoomByCinemaRoomId(seance.getCinemaRoomId());
                    String cinemaName = cinemaService.getNameByCinemaId(cinemaRoomName.getCinemaId());
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
        Reservation seanceReservation = reservationService.getReservationByReservationId(userReservationList.get(reservation - 1).getReservationId());
        SeatsSeance seatsSeance = seatSeanceService.getSeatSeancesBySeatId(seanceReservation.getSeatId());
        buyTickets(seanceReservation.getSeanceId(), seatsSeance);
        reservationService.deleteReservation(seanceReservation.getId());
    }

    private void option5() {
        System.out.println("___ Add Seance ___");
        if (cinemaRoomService.getAll().isEmpty()) {
            System.out.println("No cinema rooms in database. Add a new cinema room before adding a seance");
            return;
        }
        if (movieService.getAll().isEmpty()) {
            System.out.println("No movies in database. Add a new movie before adding a new seance");
            return;
        }
        var seance = CreateSeanceDto
                .builder()
                .cinemaRoomId(UserDataService.getCinemaRoom("Type cinema room id", cinemaRoomService).getId())
                .movieId(UserDataService.getMovie("Type movie id", movieService).getId())
                .dateTime(UserDataService.getLocalDateTime("Type seance date"))
                .build();
        System.out.println("Seance added with id: " + adminService.addSeance(seance));
    }

    private void option4() {
        System.out.println("___ Add Cinema Rooms ___");
        if (cinemaService.getAll().isEmpty()) {
            System.out.println("No cinemas in database. Add a cinema before adding cinema room");
            return;
        }
        var cinemaRoom = CreateCinemaRoomDto
                .builder()
                .name(UserDataService.getString("Type cinema room name"))
                .rows(UserDataService.getInteger("Type cinema room rows"))
                .places(UserDataService.getInteger("Type cinema room places"))
                .cinemaId(UserDataService.getCinema("Type cinema id", cinemaService).getId())
                .build();
        System.out.println(cinemaRoom.getName() + " added with id: " + adminService.addCinemaRoom(cinemaRoom));
    }

    private void option3() {
        System.out.println("___ Add Cinema ___");
        if (cityService.getAll().isEmpty()) {
            System.out.println("No cities in database. Add a city before adding a new cinema");
            return;
        }
        var cinema = CreateCinemaDto
                .builder()
                .cityId(UserDataService.getCity("Type city id:", cityService).getId())
                .name(UserDataService.getString("Type cinema name"))
                .build();
        System.out.println(cinema.getName() + " added with id: " + adminService.addCinema(cinema));
    }

    private void option2() {
        System.out.println("___ Add Movie ___");
        var movie = CreateMovieDto
                .builder()
                .title(UserDataService.getString("Type movie title:"))
                .genre(UserDataService.getMovieGenre("Type movie genre"))
                .dateFrom(UserDataService.getLocalDateTime("Date from"))
                .dateTo(UserDataService.getLocalDateTime("Date to"))
                .build();
        System.out.println(movie.getTitle() + " added with id: " + adminService.addMovie(movie));
    }

    private void option1() {
        System.out.println("___ Buy / Reserve Ticket ___");
        if (seanceService.getAll().isEmpty()) {
            System.out.println("There is no seances!");
            return;
        }
        String phrase = UserDataService.getString("Type cinema, city or movie title");
        AtomicInteger counter = new AtomicInteger();
        int decision;
        List<Seance> seances = seanceService.findByPhrase(phrase);
        if (seances.isEmpty()) {
            System.out.println("No cinema, city or movie with this phrase");
            return;
        }

        do {
            System.out.println("Available seances:");
            seances
                    .stream()
                    .map(seance -> counter.incrementAndGet() + ". " + movieService.showTitle(seance.getMovieId()) +
                            " (" + seance.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")" +
                            "[" + seance.getDateTime().getDayOfWeek() + "] - "
                            + cinemaService.findByCinemaRoomId(seance.getCinemaRoomId()).getName() + ", "
                            + cityService.findCityById(cinemaService.findByCinemaRoomId(seance.getCinemaRoomId()).getCityId()).getName() + ", "
                            + cinemaRoomService.getNameByCinemaRoomId(seance.getCinemaRoomId()))
                    .forEach(System.out::println);
            decision = UserDataService.getInteger("Choose seance");
        } while (decision < 1 || decision > seances.size());
        Seance seance = seances.get(decision - 1);
        long availableSeats = seatSeanceService
                .getSeatsSeancesListBySeanceId(seance.getId())
                .stream()
                .filter(seatsSeance -> seatsSeance.getState().equals(SeatState.FREE))
                .count();
        if (availableSeats < 1) {
            System.out.println("No seats available for this seance");
            return;
        }
        do {
            decision = UserDataService.getInteger("How many tickets do you want to buy or reserve? (available: " + availableSeats + ")");
        } while (decision > availableSeats || decision < 1);
        if (decision == 1) {
            chooseCinemaRoomPlace(seance.getId(), decision);
        } else {
            ///kupowanie grupowe
        }
    }

    private void chooseCinemaRoomPlace(Integer seanceId, Integer ticketsNumber) {
        Seance seance = seanceService.getSeanceById(seanceId).orElseThrow(() -> new MenuServiceException("FAILED"));
        int cinemaRoomId = seance.getCinemaRoomId();
        showCinemaRoomPlacesForSeance(seanceId);
        int rowNumber;
        int placeNumber;
        do {
            rowNumber = UserDataService.getInteger("Type row number");
            placeNumber = UserDataService.getInteger("Type place number at " + rowNumber + " row");
        }
        while (seatService.findByRowAndPlaceAtCinemaRoom(rowNumber, placeNumber, cinemaRoomId).isEmpty());
        System.out.println("PLACE: " + rowNumber + "/" + placeNumber);
        int chosenSeatId = seatService.getSeatId(rowNumber, placeNumber, cinemaRoomId);

        SeatsSeance seatSeance = seatSeanceService.getSeatSeancesBySeatId(chosenSeatId);

        if (!seatSeance.getState().equals(SeatState.FREE)) {
            System.out.println("This place is not free. Can't buy or reserve ticket!");
            return;
        }
        int decision;
        do {
            decision = UserDataService.getInteger("1 - buy / 2 - reserve");
        } while (decision != 1 && decision != 2);
        if (decision == 1) {
            buyTickets(seanceId, seatSeance);
        } else {
            reserveTicket(seanceId, seatSeance);
        }
        adminService.editSeatSeance(seatSeance);
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
        reservationService.addReservation(reservation);
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
                adminService.editSeatSeance(chosenSeatSeance);
            }
            case 2 -> {
                return;
            }
            default -> System.out.println("Wrong option");
        }
    }

    private void showCinemaRoomPlacesForSeance(Integer seanceId) {
//        int cinemaRoomId = seanceService
//                .getSeanceById(seanceId)
//                .orElseThrow(() -> new MenuServiceException("Failed"))
//                .getCinemaRoomId();
//
//        int placesAtCinemaRoom = cinemaRoomService
//                .getCinemaRoomByCinemaRoomId(cinemaRoomId)
//                .getPlaces();

        List<SeatsSeance> seatsSeances = seatSeanceService
                .getSeatsSeancesListBySeanceId(seanceId);

        Integer lastRow = seatsSeances
                .stream()
                .max(Comparator.comparing(s -> seatService.getSeat(s.getSeatId()).getRowsNumber()))
                .map(s -> seatService.getSeat(s.getSeatId()).getRowsNumber())
                .orElseThrow(() -> new MenuServiceException("FAILED"));

        Integer lastPlace = seatsSeances
                .stream()
                .max(Comparator.comparing(s -> seatService.getSeat(s.getSeatId()).getPlace()))
                .map(s -> seatService.getSeat(s.getSeatId()).getPlace())
                .orElseThrow(() -> new MenuServiceException("FAILED"));

        int seatId = 0;

        int rowsCounter = 1;
        int placeCounter = 1;

        for (int i = 0; i <= lastRow; i++) {
            for (int j = 0; j <= lastPlace; j++) {
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
