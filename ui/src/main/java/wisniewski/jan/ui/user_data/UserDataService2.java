package wisniewski.jan.ui.user_data;

import wisniewski.jan.persistence.enums.Genre;
import wisniewski.jan.persistence.enums.SearchCriterion;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.persistence.repository.*;
import wisniewski.jan.persistence.repository.impl.CinemaRepositoryImpl;
import wisniewski.jan.persistence.repository.impl.CityRepositoryImpl;
import wisniewski.jan.service.exception.UserDataServiceException;
import wisniewski.jan.service.exception.UserServiceException;
import wisniewski.jan.ui.exceptions.MenuServiceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserDataService2 {

    private final static Scanner sc = new Scanner(System.in);

    public static Integer getInteger(String msg) {
        System.out.println(msg);
        String value = sc.nextLine();
        if (!value.matches("\\d+")) {
            throw new MenuServiceException("value is not a number");
        }
        return Integer.parseInt(value);
    }

    public static SearchCriterion getSearchCriterion(String msg) {
        System.out.println(msg);
        AtomicInteger counter = new AtomicInteger();
        String sortCriterionList = Arrays
                .asList(SearchCriterion.values())
                .stream()
                .map(criterion -> counter.incrementAndGet() + ". " + criterion.toString())
                .collect(Collectors.joining("\n"));
        System.out.println(sortCriterionList);
        int decision;
        do {
            decision = getInteger("Choose correct search criterion number:");
        } while (decision < 1 || decision > SearchCriterion.values().length);
        return SearchCriterion.values()[decision - 1];
    }

    public static String getString(String msg) {
        System.out.println(msg);
        return sc.nextLine();
    }

    public static Genre getMovieGenre(String msg) {
        System.out.println(msg);
        AtomicInteger counter = new AtomicInteger();
        String showGenres = Arrays
                .asList(Genre.values())
                .stream()
                .map(criterion -> counter.incrementAndGet() + ". " + criterion.toString())
                .collect(Collectors.joining("\n"));
        System.out.println(showGenres);
        int decision;
        do {
            decision = getInteger("Choose correct search criterion number:");
        } while (decision < 1 || decision > Genre.values().length);
        return Genre.values()[decision - 1];
    }

    public static LocalDateTime getLocalDateTime() {
        String userInput;
        do {
            userInput = getString("Type date in format: yyyy-MM-dd HH:mm:ss");
        } while (!userInput.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
        return LocalDateTime.parse(userInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static City getCity(String msg, CityRepository cityRepository) {
        System.out.println(msg);
        String cities = cityRepository.findAll()
                .stream()
                .map(city -> city.getId() + ". " + city.getName())
                .collect(Collectors.joining("\n"));
        System.out.println(cities);
        int decision;
        do {
            decision = getInteger("Choose correct city number:");
        } while (cityRepository.findById(decision).isEmpty());
        return cityRepository.findById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static Cinema getCinema(String msg, CinemaRepository cinemaRepository) {
        System.out.println(msg);
        String cinemas = cinemaRepository.findAll()
                .stream()
                .map(cinema -> cinema.getId() + ". " + cinema.getName())
                .collect(Collectors.joining("\n"));
        System.out.println(cinemas);
        int decision;
        do {
            decision = getInteger("Choose correct cinema number:");
        } while (cinemaRepository.findById(decision).isEmpty());
        return cinemaRepository.findById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static CinemaRoom getCinemaRoom(String msg, CinemaRoomRepository cinemaRoomRepository) {
        System.out.println(msg);
        String cinemasRooms = cinemaRoomRepository.findAll()
                .stream()
                .map(cinemaRoom -> cinemaRoom.getId() + ". " + cinemaRoom.getName())
                .collect(Collectors.joining("\n"));
        System.out.println(cinemasRooms);
        int decision;
        do {
            decision = getInteger("Choose correct cinema room number:");
        } while (cinemaRoomRepository.findById(decision).isEmpty());
        return cinemaRoomRepository.findById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static Movie getMovie(String msg, MovieRepository movieRepository) {
        System.out.println(msg);
        String cinemasRooms = movieRepository.findAll()
                .stream()
                .map(movie -> movie.getId() + ". " + movie.getTitle())
                .collect(Collectors.joining("\n"));
        System.out.println(cinemasRooms);
        int decision;
        do {
            decision = getInteger("Choose correct movie number:");
        } while (movieRepository.findById(decision).isEmpty());
        return movieRepository.findById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static Seance getSeance(String msg, SeanceRepository seanceRepository, MovieRepository movieRepository) {
        System.out.println(msg);
        String seances = seanceRepository.findAll()
                .stream()
                .map(seance -> seance.getId() + ". " +
                        movieRepository.findById(seance.getMovieId())
                                .orElseThrow(() -> new UserDataServiceException("FAILED!"))
                                .getTitle()
                )
                .collect(Collectors.joining("\n"));
        System.out.println(seances);
        int decision;
        do {
            decision = getInteger("Choose correct seance number:");
        } while (seanceRepository.findById(decision).isEmpty());
        return seanceRepository.findById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static boolean getBoolean(String message) {
        String value;
        do {
            value = getString(message + " [y/n]");
        } while (value.toLowerCase().charAt(0) != 'y' && value.toLowerCase().charAt(0) != 'n');
        char decision = value.charAt(0);
        return Character.toLowerCase(decision) == 'y';
    }

    public void close() {
        if (sc != null) {
            sc.close();
        }
    }
}
