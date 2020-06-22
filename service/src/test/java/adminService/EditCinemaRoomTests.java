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
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.SeatRepository;
import wisniewski.jan.service.AdminService;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(LoggerExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EditCinemaRoomTests {

    @InjectMocks
    AdminService adminService;

    @Mock
    SeatRepository seatRepository;

    @Mock
    CinemaRoomRepository cinemaRoomRepository;

    @Test
    @DisplayName("adding rows to cinema is successful")
    public void test1() {
        var cinemaRoom = CinemaRoom
                .builder()
                .rowsNumber(2)
                .places(2)
                .name("Kino")
                .cinemaId(1)
                .build();

        var cinemaRoomFromDb = CinemaRoom
                        .builder()
                        .rowsNumber(1)
                        .places(2)
                        .name("Kino")
                        .cinemaId(1)
                        .build();

        Mockito
                .when(cinemaRoomRepository.findById(1))
                .thenReturn(Optional.of(cinemaRoomFromDb));

        Mockito
                .when(seatRepository.findAllByCinemaId(cinemaRoom))
                .thenReturn(List.of(
                        Seat
                                .builder()
                                .rowsNumber(1)
                                .place(1)
                                .cinemaRoomId(1)
                                .id(1)
                                .build(),
                        Seat
                                .builder()
                                .rowsNumber(1)
                                .place(2)
                                .cinemaRoomId(1)
                                .id(2)
                                .build()
                ));


    }

}
