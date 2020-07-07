package wisniewski.jan.ui.user_data;

import wisniewski.jan.persistence.enums.Genre;
import wisniewski.jan.service.enums.SearchCriterion;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.service.service.*;
import wisniewski.jan.ui.exceptions.MenuServiceException;

import java.math.BigDecimal;
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
        AtomicInteger counter = new AtomicInteger();
        String cities = cityService
                .getAll()
                .stream()
                .map(city -> counter.incrementAndGet() + ". " + city.getName())
                .collect(Collectors.joining("\n"));
        int decision;
        do {
            System.out.println(cities);
            decision = getInteger("Choose correct city number:");
        } while (decision < 1 || decision > cityService.getAll().size());
        return cityService.getAll().get(decision - 1);
    }

    public static Cinema getCinema(String msg, CinemaService cinemaService) {
        System.out.println(msg);
        AtomicInteger counter = new AtomicInteger();
        String cinemas = cinemaService
                .getAll()
                .stream()
                .map(cinema -> counter.incrementAndGet() + ". " + cinema.getName())
                .collect(Collectors.joining("\n"));
        int decision;
        do {
            System.out.println(cinemas);
            decision = getInteger("Choose correct cinema number:");
        } while (decision < 1 || decision > cinemaService.getAll().size());
        return cinemaService.getAll().get(decision - 1);
    }

    public static CinemaRoom getCinemaRoom(String msg, CinemaRoomService cinemaRoomService) {
        System.out.println(msg);
        AtomicInteger counter = new AtomicInteger();
        String cinemasRoom = cinemaRoomService
                .getAll()
                .stream()
                .map(cinemaRoom -> counter.incrementAndGet() + ". " + cinemaRoom.getName())
                .collect(Collectors.joining("\n"));
        int decision;
        do {
            System.out.println(cinemasRoom);
            decision = getInteger("Choose correct cinema room number:");
        } while (decision < 1 || decision > cinemaRoomService.getAll().size());
        return cinemaRoomService.getAll().get(decision - 1);
    }

    public static Movie getMovie(String msg, MovieService movieService) {
        System.out.println(msg);
        AtomicInteger counter = new AtomicInteger();
        String movies = movieService
                .getAll()
                .stream()
                .map(movie -> counter.incrementAndGet() + ". " + movie.getTitle())
                .collect(Collectors.joining("\n"));
        int decision;
        do {
            System.out.println(movies);
            decision = getInteger("Choose correct movie number:");
        } while (decision < 1 || decision > movieService.getAll().size());
        return movieService.getAll().get(decision - 1);
    }

    public static Seance getSeance(String msg, SeanceService seanceService) {
        System.out.println(msg);
        AtomicInteger counter = new AtomicInteger();
        String seances = seanceService
                .getAll()
                .stream()
                .map(seance -> counter.incrementAndGet() + ". " + seance.getMovieId())
                .collect(Collectors.joining("\n"));
        int decision;
        do {
            System.out.println(seances);
            decision = getInteger("Choose correct seance number:");
        } while (decision < 1 || decision > seanceService.getAll().size());
        return seanceService.getAll().get(decision - 1);
    }

    public static boolean getBoolean(String message) {
        String value;
        do {
            value = getString(message + " [y/n]");
        } while (value.toLowerCase().charAt(0) != 'y' && value.toLowerCase().charAt(0) != 'n');
        char decision = value.charAt(0);
        return Character.toLowerCase(decision) == 'y';
    }

    public static BigDecimal getDecimal(String message) {
        String value;
        do {
            value = getString(message);
        } while (!value.matches("\\d{1,3}\\.?\\d+"));
        return new BigDecimal(value);
    }

    public void close() {
        if (sc != null) {
            sc.close();
        }
    }
}
