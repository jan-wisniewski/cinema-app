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
import wisniewski.jan.service.dto.CreateCinemaRoomDto;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.SeatRepository;
import wisniewski.jan.service.service.AdminService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({MockitoExtension.class, LoggerExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddCinemaRoomTests {

    private Logger logger;
    private String exceptionMessage;

    @InjectMocks
    AdminService adminService;

    @Mock
    CinemaRoomRepository cinemaRoomRepository;

    @Mock
    SeatRepository seatRepository;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Test
    @DisplayName("when cinemaRoomDto is null exception has been thrown")
    public void test1() {
        exceptionMessage = "";
        try {
            adminService.addCinema(null);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Cinema dto is null", exceptionMessage);
        logger.info("Failed to create cinema room: dto is null");
    }

    @Test
    @DisplayName("when cinema room data is correct adding is successful")
    public void test2() {
        CreateCinemaRoomDto cinemaRoomDto = CreateCinemaRoomDto
                .builder()
                .rows(5)
                .name("Room")
                .places(8)
                .cinemaId(5)
                .build();

        var expectedCinema = CinemaRoom
                .builder()
                .rowsNumber(5)
                .name("Room")
                .places(8)
                .cinemaId(5)
                .id(1)
                .build();

        var cinemaToAdd = Mapper.fromCinemaRoomDtoToCinemaRoom(cinemaRoomDto);

        //symuluje dzialanie metody dodajacej do bazy
        //jezeli dodalbym dto to udaje sobie ze mi zwroci cinemaroom z id nr 1
        Mockito
                .when(cinemaRoomRepository.add(cinemaToAdd))
                .thenReturn(Optional.of(expectedCinema));

        var seatToAdd = Seat
                .builder()
                .rowsNumber(1)
                .place(1)
                .cinemaRoomId(5)
                .build();

        Mockito
                .when(seatRepository.addAll(List.of(seatToAdd)))
                .thenReturn(List.of(seatToAdd));

        assertEquals(expectedCinema.getId(), adminService.addCinemaRoom(cinemaRoomDto));
        logger.info("Cinema room created successfully");
    }

    @Test
    @DisplayName("If rows number is negative or equal zero exception has been thrown")
    public void test3() {
        var cinemaRoomDto = CreateCinemaRoomDto
                .builder()
                .rows(0)
                .name("Room1")
                .places(8)
                .cinemaId(5)
                .build();
        exceptionMessage = "";
        try {
            adminService.addCinemaRoom(cinemaRoomDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add cinema room errors: Rows : Should be greater than 0", exceptionMessage);
        logger.info("Failed to create cinema room: rows number is not correct");
    }

    @Test
    @DisplayName("if places number is negative or equal zero exception has been thrown")
    public void test4() {
        var cinemaRoomDto = CreateCinemaRoomDto
                .builder()
                .rows(2)
                .name("Room1")
                .places(-1)
                .cinemaId(5)
                .build();
        exceptionMessage = "";
        try {
            adminService.addCinemaRoom(cinemaRoomDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add cinema room errors: Places : Should be greater than 0", exceptionMessage);
        logger.info("Failed to create cinema room: place number is not correct");
    }

    @Test
    @DisplayName("when name starts with lowercase exception has been thrown")
    public void test5() {
        var cinemaRoomDto = CreateCinemaRoomDto
                .builder()
                .rows(2)
                .name("room")
                .places(3)
                .cinemaId(5)
                .build();
        exceptionMessage = "";
        try {
            adminService.addCinemaRoom(cinemaRoomDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add cinema room errors: Name : Should starts with uppercase", exceptionMessage);
        logger.info("Failed to create cinema room: name starts with lowercase");
    }

    @Test
    @DisplayName("when name length is equal 1 or 0 exception has been thrown")
    public void test6() {
        var cinemaRoomDto = CreateCinemaRoomDto
                .builder()
                .rows(2)
                .name("r")
                .places(3)
                .cinemaId(5)
                .build();
        exceptionMessage = "";
        try {
            adminService.addCinemaRoom(cinemaRoomDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Add cinema room errors: Name : Should be longer than one char", exceptionMessage);
        logger.info("Failed to create cinema room: name length is 1 or 0");
    }

    //when is already on db
    @Test
    @DisplayName("when cinema room with this name and city id is already on db exception has been thrown")
    public void test7() {
        var cinemaRoomDto = CreateCinemaRoomDto
                .builder()
                .rows(2)
                .name("Room1")
                .places(3)
                .cinemaId(5)
                .build();

        List<CinemaRoom> cinemasRoomListFromDB = List.of(
                CinemaRoom.builder().rowsNumber(5).places(2).name("Room1").cinemaId(5).build(),
                CinemaRoom.builder().rowsNumber(6).places(6).name("Room2").cinemaId(2).build()
        );

        Mockito
                .when(cinemaRoomRepository.findByNameAndCinemaId("Room1", 5))
                .thenReturn(Optional.of(cinemasRoomListFromDB.get(0)));

        exceptionMessage = "";
        try {
            adminService.addCinemaRoom(cinemaRoomDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Cinema room with this name is already on this cinema id", exceptionMessage);
        logger.info("Failed to create cinema room: cinema room already exists on db");
    }
}
