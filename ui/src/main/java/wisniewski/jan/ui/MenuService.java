package wisniewski.jan.ui;

import wisniewski.jan.persistence.enums.SearchCriterion;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.MovieRepository;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.ui.exceptions.MenuServiceException;
import wisniewski.jan.ui.user_data.UserDataService;

import java.util.List;
import java.util.stream.Collectors;

public class MenuService {

    private CinemaRepository cinemaRepository;
    private CinemaRoomRepository cinemaRoomRepository;
    private SeanceRepository seanceRepository;
    private MovieRepository movieRepository;

    public MenuService(CinemaRepository cinemaRepository, CinemaRoomRepository cinemaRoomRepository, SeanceRepository seanceRepository, MovieRepository movieRepository) {
        this.cinemaRepository = cinemaRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.seanceRepository = seanceRepository;
        this.movieRepository = movieRepository;
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
        System.out.println("___ Buy ticket ___");
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
                seanceRepository.findSeancesByCinemaRooms(cinemaRooms)
                        .stream()
                        .map(seance -> movieRepository
                                .findById(seance.getMovieId())
                                .orElseThrow(() -> new MenuServiceException("Movie id not found"))
                        )
                        .forEach(System.out::println);
                Integer seanceId = UserDataService.getInteger("Type seance id");
                int cinemaRoomId = seanceRepository
                        .findById(seanceId)
                        .orElseThrow(() -> new MenuServiceException("Failed!"))
                        .getCinemaRoomId();
                int placesAtCinemaRoom = cinemaRoomRepository
                        .findById(cinemaId)
                        .orElseThrow(() -> new MenuServiceException("Failed 2!"))
                        .getRowsNumber();
                //tablica z miejscami
                //pobierz seat_id
                //pobierz user_id
                //wylicz cene i akcept
                //ticketService.buyTicket()


                //ewentualnie zajrzec do pdf


            }
            case CITY -> filter = UserDataService.getString("Type city name");
            case SEANCE -> filter = UserDataService.getString("Type seance name");
        }
        // System.out.println(filter);
    }

}
