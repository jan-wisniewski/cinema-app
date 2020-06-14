package wisniewski.jan.persistence.connection;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Batch;

import java.util.List;

public class DbConnection {
    private final String username;
    private final String password;
    private final String url;
    private final Jdbi jdbi;

    public DbConnection(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.jdbi = Jdbi.create(url, password, username);
    }

    public Jdbi getJdbi() {
        return jdbi;
    }

    public void setUpTables() {

        var SEANCES = """
                create table if not exists seances (
                id integer primary key auto_increment,
                movie_id integer not null,
                cinema_room_id integer not null,
                date_time date not null
                );
                """;
        var MOVIES = """
                create table if not exists movies (
                id integer primary key auto_increment,
                title varchar(50) not null,
                genre varchar(50) not null,
                date_from date not null,
                date_to date not null
                );
                """;
        var USERS = """
                create table if not exists users ( 
                id integer primary key auto_increment,
                name varchar (50) not null,
                surname varchar(50) not null
                );
                """;
        var CITIES_TABLE = """
                create table if not exists cities (
                id integer primary key auto_increment,
                name varchar (50) not null
                );
                """;
        var TICKETS_TABLE = """
                create table if not exists tickets (
                id integer primary key auto_increment,
                seance_id integer not null,
                seat_id integer not null,
                price decimal default (0),
                discount double,
                user_id integer not null,
                foreign key (seance_id) references on seances(id) on delete cascade on update cascade,
                foreign key (seat_id) references on seats(id) on delete cascade on update cascade,
                foreign key (user_id) references on users(id) on delete cascade on update cascade
                );
                """;

        var SEATS_SEANCES = """
                create table if not exists seats_seances (
                id integer primary key auto_increment,
                seat_id integer not null,
                seance_id integer not null,
                state varchar(50) not null,
                foreign key (seat_id) references on seats(id) on delete cascade on update cascade,
                foreign key (seance_id) references on seances(id) on delete cascade on update cascade,
                );
                """;


        var SEATS_TABLE = """
                create table if not exists seats (
                id integer primary key auto_increment,
                roww integer not null,
                place integer,
                cinema_room_id integer,
                foreign key (cinema_room_id) references on cinema_rooms(id) on delete cascade on update cascade
                );
                """;

        var CINEMA_ROOMS_TABLE = """
                create table if not exists cinema_rooms (
                id integer primary key auto_increment,
                name varchar(50) not null,
                cinema_id integer not null,
                rows integer not null,
                places integer not null,
                foreign key (cinema_id) references on cinemas(id) on delete cascade on update cascade
                );
                 """;
        var CINEMAS_TABLE = """
                create table if not exists cinemas (
                id integer primary key auto_increment,
                name varchar(50) not null,
                city_id integer not null,
                foreign key (city_id) references on cities(id) on delete cascade on update cascade
                );
                """;
        jdbi
                .useHandle(handle -> {
                    Batch batch = handle.createBatch();
//                    List<String> tables = List.of(
//                            SEATS_TABLE, CINEMA_ROOMS_TABLE, CINEMAS_TABLE,
//                            CITIES_TABLE, TICKETS_TABLE,
//                            SEANCES, MOVIES, USERS
//                    );
//                    tables.forEach(batch::add);

                    batch.add(CITIES_TABLE);
                    batch.add(SEANCES);
                    batch.add(MOVIES);
                    batch.add(USERS);
                    batch.add(SEATS_SEANCES);

                    batch.add(SEATS_TABLE);

                    batch.add(CINEMA_ROOMS_TABLE);
                    batch.add(CINEMAS_TABLE);
                    batch.add(TICKETS_TABLE);
                    batch.execute();
                });
    }
}