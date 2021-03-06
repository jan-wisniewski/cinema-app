package adminService;

import extensions.LoggerExtension;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import wisniewski.jan.service.dto.CreateCinemaDto;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.service.exception.AdminServiceException;
import wisniewski.jan.service.service.CinemaService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoggerExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddCinemaTests {

    private Logger logger;
    private String exceptionMessage;

    @Mock
    CinemaService cinemaService;

    @Mock
    CinemaRepository cinemaRepository;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Test
    @DisplayName("when create cinema dto is null exception has been thrown")
    public void test1() {
        exceptionMessage = "";
        try {
            cinemaService.addCinema(null);
        } catch (Exception e){
            exceptionMessage = e.getMessage();
        }
        assertEquals("Cinema dto is null", exceptionMessage);
        logger.info("Failed to create cinema: dto is null");
    }

    @Test
    @DisplayName("when cinema data is correct nothing will be throw")
    public void test2() {
        var cinemaDto = CreateCinemaDto
                .builder()
                .cityId(2)
                .name("KINO A")
                .build();

        var expected = Cinema
                .builder()
                .cityId(2)
                .name("KINO A")
                .id(1)
                .build();

        var cinemaToAdd = Mapper.fromCinemaDtoToCinema(cinemaDto);

        //syluje dzialanie metody dodajacej do bazy
        //jezeli dodalbym dto to udaje sobie ze mi zwroci cinema z id nr 1
        Mockito
                .when(cinemaRepository.add(cinemaToAdd))
                .thenReturn(Optional.of(expected));

        assertEquals(expected.getId(), cinemaService.addCinema(cinemaDto));
        logger.info("Cinema created successfully");

    }

    @Test
    @DisplayName("when cinema name is lowercase exception has been thrown")
    public void test3() {
        var cinema = CreateCinemaDto
                .builder()
                .cityId(2)
                .name("aaa")
                .build();
        assertThrows(AdminServiceException.class, () -> cinemaService.addCinema(cinema));
        logger.info("Failed to create cinema: lowercase name ");
    }

    //czy kino jest w db
    @Test
    @DisplayName("when cinema is already on db exception has been thrown")
    public void test4() {
        var cinema = CreateCinemaDto
                .builder()
                .cityId(2)
                .name("Abc")
                .build();
        List<Cinema> cinemas = List.of(
                Cinema.builder().cityId(2).id(1).name("Abc").build(),
                Cinema.builder().cityId(3).id(2).name("DAF").build()
        );
        Mockito
                .when(cinemaRepository.findAll())
                .thenReturn(cinemas);
        Mockito
                .when(cinemaRepository.findByName("Abc"))
                .thenReturn(Optional.of(cinemas.get(0)));
        exceptionMessage = "";
        try {
            cinemaService.addCinema(cinema);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Cinema with this name is already on DB", exceptionMessage);
        logger.info("Failed to create cinema: cinema already on db");
    }
}