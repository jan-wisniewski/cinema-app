package adminService;

import extensions.LoggerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.apache.log4j.Logger;
import wisniewski.jan.service.dto.CreateSeanceDto;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.service.repository.SeanceRepository;
import wisniewski.jan.service.service.AdminService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoggerExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddSeanceTests {

    private String exceptionMessage;

    @InjectMocks
    AdminService adminService;

    @Mock
    SeanceRepository seanceRepository;

    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Test
    @DisplayName("when seance dto is null exception has been thrown")
    public void test1() {
        exceptionMessage = "";
        try {
            adminService.addSeance(null);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("seance dto is null", exceptionMessage);
        logger.info("Failed to create seance: dto is null");
    }

    @Test
    @DisplayName("when local date is not correct exception has been thrown")
    public void test2() {
        var seance = CreateSeanceDto
                .builder()
                .cinemaRoomId(4)
                .movieId(3)
                .dateTime(LocalDateTime.now())
                .build();

        Mockito
                .when(seanceRepository.isMovieDisplayed(seance))
                .thenReturn(true);

        exceptionMessage = "";
        try {
            adminService.addSeance(seance);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add seance errors: Local Date : Time is before current date", exceptionMessage);
        logger.info("Failed to create seance: localdate is not correct");
    }

    @Test
    @DisplayName("when seance data is correct adding is successful")
    public void test3() {
        var seanceDto = CreateSeanceDto
                .builder()
                .cinemaRoomId(4)
                .movieId(3)
                .dateTime(LocalDateTime.now().plusDays(2))
                .build();
        var seanceExpected = Seance
                .builder()
                .id(1)
                .cinemaRoomId(4)
                .movieId(3)
                .dateTime(LocalDateTime.now().plusDays(2))
                .build();

        var seanceToAdd = Mapper.fromSeanceDtoToSeance(seanceDto);

        Mockito
                .when(seanceRepository.add(seanceToAdd))
                .thenReturn(Optional.of(seanceExpected));


        Mockito
                .when(seanceRepository.isMovieDisplayed(seanceDto))
                .thenReturn(true);


        assertEquals(seanceExpected.getId(), adminService.addSeance(seanceDto));
        logger.info("Create seance successfully");
    }

    @Test
    @DisplayName("when seance already exist in db exception has been thrown")
    public void test4() {
        var seanceDto = CreateSeanceDto
                .builder()
                .cinemaRoomId(4)
                .movieId(3)
                .dateTime(LocalDateTime.now().plusDays(2))
                .build();
        var seanceExpected = Seance
                .builder()
                .id(1)
                .cinemaRoomId(4)
                .movieId(3)
                .dateTime(LocalDateTime.now().plusDays(2))
                .build();

        var seanceMapped = Mapper.fromSeanceDtoToSeance(seanceDto);

        Mockito
                .when(seanceRepository.add(seanceMapped))
                .thenReturn(Optional.of(seanceExpected));

        Mockito
                .when(seanceRepository.isUniqueSeance(seanceDto))
                .thenReturn(Optional.of(seanceExpected));

        exceptionMessage = "";
        try {
            adminService.addSeance(seanceDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Seance is not unique. The same movieId, cinemaRoomId i LocalDate", exceptionMessage);
        logger.info("Failed to create seance: seance already exists on db");
    }

    @Test
    @DisplayName("when movie date to is before seance date exception has been thrown")
    public void test5() {
        CreateSeanceDto seanceDto = CreateSeanceDto
                .builder()
                .dateTime(LocalDateTime.now().plusDays(4))
                .movieId(1)
                .cinemaRoomId(1)
                .build();

        Mockito
                .when(seanceRepository.isMovieDisplayed(seanceDto))
                .thenReturn(false);

        exceptionMessage = "";
        try {
            adminService.addSeance(seanceDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Screening date is not within the time frame of the movie", exceptionMessage);
        logger.info("Failed to create seance: movie date to is before seance date");
    }

    @Test
    @DisplayName("when movie date to is after seance date adding is successfully")
    public void test6() {
        CreateSeanceDto seanceDto = CreateSeanceDto
                .builder()
                .dateTime(LocalDateTime.now().plusDays(4))
                .movieId(1)
                .cinemaRoomId(1)
                .build();

        var expectedSeanceFromDb = Seance
                .builder()
                .dateTime(seanceDto.getDateTime())
                .movieId(seanceDto.getMovieId())
                .cinemaRoomId(seanceDto.getCinemaRoomId())
                .id(1)
                .build();

        Mockito
                .when(seanceRepository.isUniqueSeance(seanceDto))
                .thenReturn(Optional.empty());

        Mockito
                .when(seanceRepository.isMovieDisplayed(seanceDto))
                .thenReturn(true);

        var seanceToAdd = Mapper.fromSeanceDtoToSeance(seanceDto);

        Mockito
                .when(seanceRepository.add(seanceToAdd))
                .thenReturn(Optional.of(expectedSeanceFromDb));

        assertEquals(expectedSeanceFromDb.getId(), adminService.addSeance(seanceDto));

    }

    @Test
    @DisplayName("when movie date from is before seance date adding is successfully")
    public void test7() {
        CreateSeanceDto seanceDto = CreateSeanceDto
                .builder()
                .dateTime(LocalDateTime.now().plusDays(4))
                .movieId(1)
                .cinemaRoomId(1)
                .build();

        var expectedSeanceFromDb = Seance
                .builder()
                .dateTime(seanceDto.getDateTime())
                .movieId(seanceDto.getMovieId())
                .cinemaRoomId(seanceDto.getCinemaRoomId())
                .id(1)
                .build();

        Mockito
                .when(seanceRepository.isUniqueSeance(seanceDto))
                .thenReturn(Optional.empty());

        Mockito
                .when(seanceRepository.isMovieDisplayed(seanceDto))
                .thenReturn(true);

        var seanceToAdd = Mapper.fromSeanceDtoToSeance(seanceDto);

        Mockito
                .when(seanceRepository.add(seanceToAdd))
                .thenReturn(Optional.of(expectedSeanceFromDb));

        assertEquals(expectedSeanceFromDb.getId(), adminService.addSeance(seanceDto));

    }

    @Test
    @DisplayName("when movie date from is after seance date exception has been thrown")
    public void test8() {
        CreateSeanceDto seanceDto = CreateSeanceDto
                .builder()
                .dateTime(LocalDateTime.now().plusDays(4))
                .movieId(1)
                .cinemaRoomId(1)
                .build();

        var expectedSeanceFromDb = Seance
                .builder()
                .dateTime(seanceDto.getDateTime())
                .movieId(seanceDto.getMovieId())
                .cinemaRoomId(seanceDto.getCinemaRoomId())
                .id(1)
                .build();

        Mockito
                .when(seanceRepository.isUniqueSeance(seanceDto))
                .thenReturn(Optional.empty());

        Mockito
                .when(seanceRepository.isMovieDisplayed(seanceDto))
                .thenReturn(false);

        var seanceToAdd = Mapper.fromSeanceDtoToSeance(seanceDto);

        Mockito
                .when(seanceRepository.add(seanceToAdd))
                .thenReturn(Optional.of(expectedSeanceFromDb));

        exceptionMessage = "";
        try {
            adminService.addSeance(seanceDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }

        assertEquals("Screening date is not within the time frame of the movie", exceptionMessage);

    }
}
