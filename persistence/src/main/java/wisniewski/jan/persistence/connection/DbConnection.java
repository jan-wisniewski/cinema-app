package wisniewski.jan.persistence.connection;

import org.jdbi.v3.core.Jdbi;

public class DbConnection {
    private final String username;
    private final String password;
    private final String url;
    private final Jdbi jdbi;

    public DbConnection(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.jdbi = Jdbi.create(url, username, password);
    }

    public Jdbi getJdbi() {
        return jdbi;
    }

    public void setUpTables() {
        var MOVIES = """
                create table if not exists movies (
                id integer primary key auto_increment,
                title varchar(50) not null,
                genre varchar(50) not null,
                date_from timestamp not null,
                date_to timestamp not null
                );
                """;

        var USERS = """
                create table if not exists users (
                id integer primary key auto_increment,
                name varchar (50) not null,
                surname varchar(50) not null,
                username varchar(50) not null,
                email varchar (50) not null, 
                password varchar (200) not null,
                role varchar (50) not null
                );
                """;

        var CITIES = """
                create table if not exists cities (
                id integer primary key auto_increment,
                name varchar (50) not null
                );
                """;

        var CINEMA_ROOMS = """
                create table if not exists cinema_rooms (
                id integer primary key auto_increment,
                name varchar(50) not null,
                cinema_id integer not null,
                rows_number integer not null,
                places integer not null,
                foreign key (cinema_id) references cinemas(id) on delete cascade on update cascade
                );
                 """;

        var SEANCES = """
                create table if not exists seances (
                id integer primary key auto_increment,
                movie_id integer not null,
                cinema_room_id integer not null,
                date_time timestamp,
                price decimal not null,
                foreign key (movie_id) references movies (id) on delete cascade on update cascade,
                foreign key (cinema_room_id) references cinema_rooms (id) on delete cascade on update cascade
                );
                """;

        var TICKETS = """
                create table if not exists tickets (
                id integer primary key auto_increment,
                seance_id integer not null,
                seat_id integer not null,
                price decimal default (0),
                discount double,
                user_id integer not null,
                foreign key (seance_id) references seances(id) on delete cascade on update cascade,
                foreign key (seat_id) references seats(id) on delete cascade on update cascade,
                foreign key (user_id) references users(id) on delete cascade on update cascade
                );
                """;

        var SEATS_SEANCES = """
                create table if not exists seats_seances (
                id integer primary key auto_increment,
                seat_id integer not null,
                seance_id integer not null,
                state varchar(50) not null,
                foreign key (seat_id) references seats(id) on delete cascade on update cascade,
                foreign key (seance_id) references seances(id) on delete cascade on update cascade
                );
                """;


        var SEATS_TABLE = """
                create table if not exists seats (
                id integer primary key auto_increment,
                rows_number integer not null,
                place integer,
                cinema_room_id integer,
                foreign key (cinema_room_id) references cinema_rooms(id) on delete cascade on update cascade
                );
                """;

        var CINEMAS_TABLE = """
                create table if not exists cinemas (
                id integer primary key auto_increment,
                name varchar(50) not null,
                city_id integer not null,
                foreign key (city_id) references cities(id) on delete cascade on update cascade
                );
                """;

        var RESERVATIONS_TABLE = """
                create table if not exists reservations (
                id integer primary key auto_increment not null,
                user_id integer not null,
                seance_id integer not null,
                seat_id integer not null,        
                foreign key (user_id) references users(id) on delete cascade on update cascade,
                foreign key (seance_id) references seances(id) on delete cascade on update cascade,
                foreign key (seat_id) references seats(id) on delete cascade on update cascade
                );
                """;


        jdbi
                .useTransaction(handle -> {
                    handle.execute(MOVIES);
                    handle.execute(USERS);
                    handle.execute(CITIES);
                    handle.execute(CINEMAS_TABLE);
                    handle.execute(CINEMA_ROOMS);
                    handle.execute(SEANCES);
                    handle.execute(SEATS_TABLE);
                    handle.execute(SEATS_SEANCES);
                    handle.execute(TICKETS);
                    handle.execute(RESERVATIONS_TABLE);
                });
    }
}