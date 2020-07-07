package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Seat;
import wisniewski.jan.service.repository.CinemaRoomRepository;
import wisniewski.jan.service.repository.SeatRepository;
import wisniewski.jan.service.exception.SeatServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final CinemaRoomRepository cinemaRoomRepository;

    public Seat getSeat(Integer seatId) {
        return seatRepository.findById(seatId).orElseThrow(() -> new SeatServiceException("Failed"));
    }

    public Integer getSeatId(Integer rowNumber, Integer placeNumber, Integer cinemaRoomId) {
        return seatRepository
                .findByRowAndPlaceAtCinemaRoom(rowNumber, placeNumber, cinemaRoomId)
                .orElseThrow(() -> new SeatServiceException("Failed"))
                .getId();
    }

    public Optional<Seat> findByRowAndPlaceAtCinemaRoom(Integer rowNumber, Integer placeNumber, Integer cinemaRoomId) {
        return seatRepository.findByRowAndPlaceAtCinemaRoom(rowNumber, placeNumber, cinemaRoomId);
    }

    public List<Seat> findPlacesAboveSeatPlace(CinemaRoom cinemaRoom, Integer seatPlaceNumber) {
        return seatRepository.findByPlacesAboveSeatPlace(cinemaRoom, seatPlaceNumber);
    }

    public List<Seat> findPlacesAboveRowNumber(CinemaRoom cinemaRoom, Integer rowNumber) {
        return seatRepository.findByPlacesAboveSeatRow(cinemaRoom, rowNumber);
    }

    public Integer deleteSeats (List<Seat> seats){
        return seatRepository.deleteAll(seats);
    }

    public List<Seat> addPlacesToExistsRows(CinemaRoom cinemaRoom) {
        List<Seat> generatedSeats = new ArrayList<>();
        CinemaRoom cinemaRoomFromDb = cinemaRoomRepository.findById(cinemaRoom.getId()).orElseThrow(() -> new SeatServiceException("Failed"));
        int lastPlace = cinemaRoom.getPlaces();

        for (int i = 1; i <= cinemaRoom.getRowsNumber(); i++) {
            for (int j = cinemaRoomFromDb.getPlaces() + 1; j <= lastPlace; j++) {
                generatedSeats.add(
                        Seat
                                .builder()
                                .cinemaRoomId(cinemaRoom.getId())
                                .place(j)
                                .rowsNumber(i)
                                .build()
                );
            }
        }
        return seatRepository.addAll(generatedSeats);
    }

    public List<Seat> addNewPlacesAndRows(CinemaRoom cinemaRoom) {
        CinemaRoom cinemaRoomDb = cinemaRoomRepository.findById(cinemaRoom.getId()).orElseThrow(() -> new SeatServiceException("Failed"));
        int oldRows = cinemaRoomDb.getRowsNumber();
        int oldPlaces = cinemaRoomDb.getPlaces();
        int newRows = cinemaRoom.getRowsNumber();
        int newPlaces = cinemaRoom.getPlaces();
        List<Seat> generatedSeats = new ArrayList<>();
        int currentPlace = oldPlaces + 1;
        for (int i = 1; i <= newRows; i++) {
            for (int j = 1; j <= newPlaces; j++) {
                if (currentPlace > newPlaces) {
                    currentPlace = (i < oldRows) ? oldPlaces + 1 : 1;
                    break;
                }
                if (i <= oldRows) {
                    generatedSeats.add(
                            Seat
                                    .builder()
                                    .rowsNumber(i)
                                    .place(currentPlace)
                                    .cinemaRoomId(cinemaRoomDb.getId())
                                    .build()
                    );
                    currentPlace++;
                } else {
                    generatedSeats.add(
                            Seat
                                    .builder()
                                    .rowsNumber(i)
                                    .place(j)
                                    .cinemaRoomId(cinemaRoomDb.getId())
                                    .build()
                    );
                }
            }
        }
        return seatRepository.addAll(generatedSeats);
    }

    public List<Seat> addPlacesToNewRows(CinemaRoom cinemaRoom, Integer startRow, Integer newRowsToGenerate) {
        List<Seat> generatedSeats = new ArrayList<>();
        int idRowNumber = startRow;
        for (int i = 1; i <= newRowsToGenerate; i++) {
            for (int j = 1; j <= cinemaRoom.getPlaces(); j++) {
                generatedSeats.add(
                        Seat
                                .builder()
                                .cinemaRoomId(cinemaRoom.getId())
                                .place(j)
                                .rowsNumber(idRowNumber)
                                .build()
                );
            }
            idRowNumber++;
        }
        return seatRepository.addAll(generatedSeats);
    }

    public Seat getSeatById(Integer id) {
        return seatRepository.findById(id).orElseThrow(() -> new SeatServiceException("FAILED"));
    }

}
