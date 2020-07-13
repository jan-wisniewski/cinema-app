package adminService;

import extensions.LoggerExtension;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import wisniewski.jan.persistence.enums.Genre;
import wisniewski.jan.service.dto.CreateMovieDto;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.repository.MovieRepository;
import wisniewski.jan.service.service.AdminService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LoggerExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddMovieTests {

    private Logger logger;
    private String exceptionMessage;

    @InjectMocks
    AdminService adminService;

    @Mock
    MovieRepository movieRepository;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Test
    @DisplayName("when movie dto is null exception has been thrown")
    public void test1() {
        exceptionMessage = "";
        try {
            adminService.addMovie(null);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("movieDto is null", exceptionMessage);
        logger.info("Failed to create movie: movie dto is null");
    }

    @Test
    @DisplayName("when movie title is from lowercase exception has been thrown")
    public void test2() {
        var movieDto = CreateMovieDto
                .builder()
                .title("karpaty")
                .genre(Genre.COMEDY)
                .dateFrom(LocalDateTime.now().minusDays(4))
                .dateTo(LocalDateTime.now().plusDays(10))
                .build();
        exceptionMessage = "";
        try {
            adminService.addMovie(movieDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add movie errors: Title : Should starts with uppercase", exceptionMessage);
        logger.info("Failed to create movie: title starts with lowercase");
    }

    @Test
    @DisplayName("when title is empty exception has been thrown")
    public void test3() {
        var movieDto = CreateMovieDto
                .builder()
                .title("")
                .genre(Genre.COMEDY)
                .dateFrom(LocalDateTime.now().minusDays(4))
                .dateTo(LocalDateTime.now().plusDays(10))
                .build();
        exceptionMessage = "";
        try {
            adminService.addMovie(movieDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add movie errors: Title : Title cannot be empty", exceptionMessage);
        logger.info("Failed to create movie: title is empty");
    }

    @Test
    @DisplayName("when date to is before date from exception has been thrown")
    public void test4() {
        var movieDto = CreateMovieDto
                .builder()
                .title("Film")
                .genre(Genre.COMEDY)
                .dateTo(LocalDateTime.now().minusDays(4))
                .dateFrom(LocalDateTime.now().plusDays(10))
                .build();
        exceptionMessage = "";
        try {
            adminService.addMovie(movieDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add movie errors: Date : Date to is before date from", exceptionMessage);
        logger.info("Failed to create movie: date to is before date from");
    }

    @Test
    @DisplayName("when movie is already on db exception has been thrown")
    public void test5() {
        var movieDto = CreateMovieDto
                .builder()
                .title("Tytuł")
                .dateTo(LocalDateTime.now().plusDays(10))
                .dateFrom(LocalDateTime.now())
                .genre(Genre.COMEDY)
                .build();
        var expectedMovieFromDb = Movie
                .builder()
                .title("Tytuł")
                .dateTo(LocalDateTime.now().plusDays(10))
                .dateFrom(LocalDateTime.now())
                .genre(Genre.COMEDY)
                .id(1)
                .build();
        var cinemaToAddAfterValidation = Mapper.fromMovieDtoToMovie(movieDto);

        Mockito
                .when(movieRepository.isUniqueMovie(cinemaToAddAfterValidation))
                .thenReturn(Optional.of(expectedMovieFromDb));
        exceptionMessage = "";
        try {
            adminService.addMovie(movieDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("This movie with that dateFrom and dateTo is already on db", exceptionMessage);
        logger.info("Failed to create movie: movie is already on db");
    }

    @Test
    @DisplayName("when movie data is correct adding is successful")
    public void test6() {
        var movieDto = CreateMovieDto
                .builder()
                .genre(Genre.COMEDY)
                .dateFrom(LocalDateTime.now().minusDays(3))
                .dateTo(LocalDateTime.now().plusDays(2))
                .title("Movie title")
                .build();
        var expectedMovieFromDb = Movie
                .builder()
                .title("Movie title")
                .dateTo(LocalDateTime.now().plusDays(2))
                .dateFrom(LocalDateTime.now().minusDays(3))
                .genre(Genre.COMEDY)
                .id(1)
                .build();
        var cinemaDtoAfterValidation = Mapper.fromMovieDtoToMovie(movieDto);

        Mockito
                .when(movieRepository.add(cinemaDtoAfterValidation))
                .thenReturn(Optional.of(expectedMovieFromDb));
        assertEquals(expectedMovieFromDb.getId(), adminService.addMovie(movieDto));
        logger.info("Adding movie successfully");
    }
}
