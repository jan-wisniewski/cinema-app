package wisniewski.jan.ui;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.repository.impl.*;
import wisniewski.jan.service.service.*;
import wisniewski.jan.service.service.proxy.AuthenticationService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        final String URL = "jdbc:mysql://localhost:3306/cinema_db?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Warsaw";
        final String USERNAME = "root";
        final String PASSWORD = "root";


/*        final String URL = "jdbc:mysql://db4free.net:3306/cinema_app_db?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Warsaw";
        final String USERNAME = "pw_student";
        final String PASSWORD = "yLr2IT4g3H";*/


        var dbConnection = new DbConnection(USERNAME, PASSWORD, URL);
        dbConnection.setUpTables();

        var cinemaRepository = new CinemaRepositoryImpl(dbConnection);
        var cinemaRoomRepository = new CinemaRoomRepositoryImpl(dbConnection);
        var seanceRepository = new SeanceRepositoryImpl(dbConnection);
        var movieRepository = new MovieRepositoryImpl(dbConnection);
        var seatRepository = new SeatRepositoryImpl(dbConnection);
        var seatsSeancesRepository = new SeatsSeancesRepositoryImpl(dbConnection);
        var ticketRepository = new TicketRepositoryImpl(dbConnection);
        var cityRepository = new CityRepositoryImpl(dbConnection);
        var reservationRepository = new ReservationRepositoryImpl(dbConnection);
        var userRepository = new UserRepositoryImpl(dbConnection);

        var ticketService = new TicketService(ticketRepository);
        var movieService = new MovieService(movieRepository, seanceRepository);
        var seatService = new SeatService(seatRepository, cinemaRoomRepository);
        var cinemaRoomService = new CinemaRoomService(cinemaRoomRepository, seatService, seanceRepository);
        var cityService = new CityService(cityRepository, seanceRepository);
        var reservationService = new ReservationService(reservationRepository);
        var seanceService = new SeanceService(seanceRepository, movieRepository, cinemaRoomRepository, seatsSeancesRepository, ticketRepository, seatRepository);
        var cinemaService = new CinemaService(cinemaRepository, seanceRepository, cinemaRoomRepository);
        var seatSeanceService = new SeatSeanceService(seatsSeancesRepository);
        var userService = new UserService(userRepository);

        var authenticationService = new AuthenticationService(userRepository);

        MenuService menuService = new MenuService(
                ticketService,
                movieService,
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
        Runnable runnable2 = () -> {
            try {
                System.out.println("Cleared seats seance with state reserved without reservation data: " +seatSeanceService.clearReservedSeats());
                TimeUnit.MINUTES.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        var future = ses.scheduleWithFixedDelay(runnable, 10, 10, TimeUnit.SECONDS);
        var future2 = ses.scheduleWithFixedDelay(runnable2, 10, 10, TimeUnit.SECONDS);

        menuService.mainMenu();


    }
}