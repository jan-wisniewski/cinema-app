package wisniewski.jan.ui;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.enums.SearchCriterion;
import wisniewski.jan.persistence.enums.SeatState;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.model.SeatsSeance;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.service.TicketService;
import wisniewski.jan.ui.exceptions.MenuServiceException;
import wisniewski.jan.ui.user_data.UserDataService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MenuService {

    private CinemaRepository cinemaRepository;
    private CinemaRoomRepository cinemaRoomRepository;
    private SeanceRepository seanceRepository;
    private MovieRepository movieRepository;
    private SeatsSeancesRepository seatsSeancesRepository;
    private SeatRepository seatRepository;
    private TicketService ticketService;

    public MenuService(CinemaRepository cinemaRepository, CinemaRoomRepository cinemaRoomRepository,
                       SeanceRepository seanceRepository, MovieRepository movieRepository,
                       SeatsSeancesRepository seatsSeancesRepository, SeatRepository seatRepository,
                       TicketService ticketService
    ) {
        this.cinemaRepository = cinemaRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.seanceRepository = seanceRepository;
        this.movieRepository = movieRepository;
        this.seatsSeancesRepository = seatsSeancesRepository;
        this.seatRepository = seatRepository;
        this.ticketService = ticketService;
    }

    public void mainMenu() {
        while (true) {
            try {
                System.out.println("1 - buy ticket");
                int decision = UserDataService.getInteger("Type option");
                switch (decision) {
                    case 1 -> option1();
                    default -> System.out.println("No option with this number");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MenuServiceException("Failed");
            }
        }
    }

    private void option1() {
        System.out.println("___ Buy / reserve  ticket ___");
        SearchCriterion searchCriterion = UserDataService.getSearchCriterion("Choose filter");
        String filter;
        int cinemaId;
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
                System.out.println("Available seances at: " + cinemaRepository
                        .findById(cinemaId)
                        .orElseThrow(() -> new MenuServiceException("Failed!"))
                        .getName()
                );
                List<CinemaRoom> cinemaRooms = cinemaRoomRepository.findByCinemaId(cinemaId);
                Integer seanceId;
                do {
                    seanceRepository.findSeancesByCinemaRooms(cinemaRooms)
                            .stream()
                            .map(seance -> seance.getId() + " : " +
                                    movieRepository
                                            .findById(seance.getMovieId())
                                            .orElseThrow(() -> new MenuServiceException("Movie id not found"))
                            )
                            .forEach(System.out::println);
                    seanceId = UserDataService.getInteger("Type seance id");
                } while (seanceRepository.findById(seanceId).isEmpty());
                Optional<Seance> chosenSeance = seanceRepository.findById(seanceId);
                int chosenCinemaRoomId = chosenSeance
                        .orElseThrow(() -> new MenuServiceException("FAILED"))
                        .getCinemaRoomId();
                showCinemaRoomPlaces(seanceId, cinemaId);
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

                    System.out.println("Buying tickets");
                    var ticket = CreateTicketDto
                            .builder()
                            .seanceId(seanceId)
                            .seatId(chosenSeatSeance.getSeatId())
                            .price(BigDecimal.TEN)
                            .discount(BigDecimal.ZERO)
                            .userId(1)
                            .build();
                    ticketService.buyTicket(ticket);
                    chosenSeatSeance.setState(SeatState.ORDERED);
                } else {
                    chosenSeatSeance.setState(SeatState.RESERVED);
                    System.out.println("Ticket reserved!");
                }
                seatsSeancesRepository.update(chosenSeatSeance);
            }
            case CITY -> filter = UserDataService.getString("Type city name");
            case SEANCE -> filter = UserDataService.getString("Type seance name");
        }
        // System.out.println(filter);
    }

    private void showCinemaRoomPlaces(Integer seanceId, Integer cinemaId) {
        int cinemaRoomId = seanceRepository
                .findById(seanceId)
                .orElseThrow(() -> new MenuServiceException("Failed!"))
                .getCinemaRoomId();
        int placesAtCinemaRoom = cinemaRoomRepository
                .findById(cinemaRoomId)
                .orElseThrow(() -> new MenuServiceException("Failed 2!"))
                .getRowsNumber();
        int seatId = 1;
        for (int i = 1; i <= placesAtCinemaRoom; i++) {
            for (int j = 1; j <= placesAtCinemaRoom; j++) {
                Optional<SeatsSeance> seatSeance = seatsSeancesRepository.findBySeatId(seatId);
                System.out.print(seatSeance
                        .orElseThrow(() -> new MenuServiceException("Failed"))
                        .getState().name().charAt(0)
                );
                seatId++;
            }
            System.out.println();
        }
    }
}
