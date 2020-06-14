import wisniewski.jan.persistence.connection.DbConnection;

public class App {
    public static void main(String[] args) {
        final String URL = "jdbc:mysql://localhost:3306/cinema_db?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Warsaw";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        var dbConnection = new DbConnection(USERNAME, PASSWORD, URL);
       // dbConnection.setUpTables();

       /* CinemaRepository cinemaRepository = new CinemaRepositoryImpl(dbConnection);
        AdminService adminService = new AdminService(cinemaRepository);*/
    }
}