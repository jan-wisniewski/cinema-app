import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.persistence.repository.impl.*;
import wisniewski.jan.service.service.*;
import wisniewski.jan.service.service.proxy.AuthenticationService;
import wisniewski.jan.ui.MenuService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        final String URL = "jdbc:mysql://localhost:3306/cinema_db?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Warsaw";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        var dbConnection = new DbConnection(USERNAME, PASSWORD, URL);
        dbConnection.setUpTables();

        CinemaRepository cinemaRepository = new CinemaRepositoryImpl(dbConnection);
        CinemaRoomRepository cinemaRoomRepository = new CinemaRoomRepositoryImpl(dbConnection);
        SeanceRepository seanceRepository = new SeanceRepositoryImpl(dbConnection);
        MovieRepository movieRepository = new MovieRepositoryImpl(dbConnection);
        SeatRepository seatRepository = new SeatRepositoryImpl(dbConnection);
        SeatsSeancesRepository seatsSeancesRepository = new SeatsSeancesRepositoryImpl(dbConnection);
        TicketRepository ticketRepository = new TicketRepositoryImpl(dbConnection);
        CityRepository cityRepository = new CityRepositoryImpl(dbConnection);
        ReservationRepository reservationRepository = new ReservationRepositoryImpl(dbConnection);
        UserRepository userRepository = new UserRepositoryImpl(dbConnection);

        TicketService ticketService = new TicketService(ticketRepository);
        MovieService movieService = new MovieService(movieRepository);
        CinemaRoomService cinemaRoomService = new CinemaRoomService(cinemaRoomRepository);
        CityService cityService = new CityService(cityRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);
        SeanceService seanceService = new SeanceService(seanceRepository, movieRepository);
        CinemaService cinemaService = new CinemaService(cinemaRepository);
        SeatService seatService = new SeatService(seatRepository, cinemaRoomRepository);
        SeatSeanceService seatSeanceService = new SeatSeanceService(seatsSeancesRepository);
        var userService = new UserService(userRepository);

        AdminService adminService = new AdminService(cinemaRepository, cinemaRoomRepository,
                seanceRepository, movieRepository,
                seatRepository, seatsSeancesRepository,
                cityRepository, ticketRepository, seatService);

        var authenticationService = new AuthenticationService(userRepository);

        MenuService menuService = new MenuService(
                ticketService,
                movieService,
                adminService,
                cinemaRoomService,
                cityService,
                reservationService,
                seanceService,
                cinemaService,
                seatService,
                seatSeanceService,
                userService,
                authenticationService
        );

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable runnable = () -> {
            try {
                System.out.println("Deleted reservations: "+reservationService.clearReservations(30));
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        var future2 = ses.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.MINUTES);

        menuService.mainMenu();
    }
}