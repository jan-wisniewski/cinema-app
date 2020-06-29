import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.persistence.repository.impl.*;
import wisniewski.jan.service.AdminService;
import wisniewski.jan.service.TicketService;
import wisniewski.jan.ui.MenuService;

public class App {
    public static void main(String[] args) {
        final String URL = "jdbc:mysql://localhost:3306/cinema_db?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Warsaw";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        var dbConnection = new DbConnection(USERNAME, PASSWORD, URL);
        dbConnection.setUpTables();
        dbConnection.createData();

        CinemaRepository cinemaRepository = new CinemaRepositoryImpl(dbConnection);
        CinemaRoomRepository cinemaRoomRepository = new CinemaRoomRepositoryImpl(dbConnection);
        SeanceRepository seanceRepository = new SeanceRepositoryImpl(dbConnection);
        MovieRepository movieRepository = new MovieRepositoryImpl(dbConnection);
        SeatRepository seatRepository = new SeatRepositoryImpl(dbConnection);
        SeatsSeancesRepository seatsSeancesRepository = new SeatsSeancesRepositoryImpl(dbConnection);
        TicketRepository ticketRepository = new TicketRepositoryImpl(dbConnection);
        CityRepository cityRepository = new CityRepositoryImpl(dbConnection);
        TicketService ticketService = new TicketService(ticketRepository);
        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        ReservationRepository reservationRepository = new ReservationRepositoryImpl(dbConnection);

        AdminService adminService = new AdminService(cinemaRepository, cinemaRoomRepository,
                seanceRepository, movieRepository,
                seatRepository, seatsSeancesRepository,
                cityRepository);

        MenuService menuService = new MenuService(
                cinemaRepository, cinemaRoomRepository,
                seanceRepository, movieRepository,
                seatsSeancesRepository, seatRepository,
                ticketService, adminService, cityRepository, reservationRepository
        );
        menuService.mainMenu();

    }
}