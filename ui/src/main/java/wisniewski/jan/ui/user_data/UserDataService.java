package wisniewski.jan.ui.user_data;

import wisniewski.jan.persistence.enums.Genre;
import wisniewski.jan.persistence.enums.SearchCriterion;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.service.*;
import wisniewski.jan.service.exception.UserServiceException;
import wisniewski.jan.ui.exceptions.MenuServiceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserDataService {

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

    public static LocalDateTime getLocalDateTime(String msg) {
        System.out.println(msg);
        String userInput;
        do {
            userInput = getString("Type date in format: yyyy-MM-dd HH:mm:ss");
        } while (!userInput.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
        return LocalDateTime.parse(userInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static City getCity(String msg, CityService cityService) {
        System.out.println(msg);
        String cities = cityService.showCities();
        System.out.println(cities);
        int decision;
        do {
            decision = getInteger("Choose correct city number:");
        } while (cityService.findCityById(decision).isEmpty());
        return cityService.findCityById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static Cinema getCinema(String msg, CinemaService cinemaService) {
        System.out.println(msg);
        String cinemas = cinemaService.showAllCinemas();
        System.out.println(cinemas);
        int decision;
        do {
            decision = getInteger("Choose correct cinema number:");
        } while (cinemaService.findCinemaById(decision).isEmpty());
        return cinemaService.findCinemaById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static CinemaRoom getCinemaRoom(String msg, CinemaRoomService cinemaRoomService) {
        System.out.println(msg);
        String cinemasRooms = cinemaRoomService.showAllCinemasRooms();
        System.out.println(cinemasRooms);
        int decision;
        do {
            decision = getInteger("Choose correct cinema room number:");
        } while (cinemaRoomService.findById(decision).isEmpty());
        return cinemaRoomService.findById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static Movie getMovie(String msg, MovieService movieService) {
        System.out.println(msg);
        System.out.println(movieService.showAll());
        int decision;
        do {
            decision = getInteger("Choose correct movie number:");
        } while (movieService.findById(decision).isEmpty());
        return movieService.findById(decision)
                .orElseThrow(() -> new UserServiceException("Failed"));
    }

    public static Seance getSeance(String msg, SeanceService seanceService) {
        System.out.println(msg);
        String seances = seanceService.showAll();
        System.out.println(seances);
        int decision;
        do {
            decision = getInteger("Choose correct seance number:");
        } while (seanceService.getSeanceById(decision).isEmpty());
        return seanceService.getSeanceById(decision)
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
