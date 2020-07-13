package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.service.exception.CinemaRoomException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CinemaRoomService {

    private final CinemaRoomRepository cinemaRoomRepository;

    public String getNameByCinemaRoomId(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId).orElseThrow(() -> new CinemaRoomException("Failed")).getName();
    }

    public Optional<CinemaRoom> findById(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId);
    }

    public Integer getRowsNumberByCinemaRoomId(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId).orElseThrow(() -> new CinemaRoomException("Failed")).getRowsNumber();
    }

    public Integer getPlacesNumberInRowByCinemaRoomId(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId).orElseThrow(() -> new CinemaRoomException("Failed")).getPlaces();
    }

    public CinemaRoom getCinemaRoomByCinemaRoomId(Integer cinemaRoomId) {
        return cinemaRoomRepository.findById(cinemaRoomId).orElseThrow(() -> new CinemaRoomException("Failed"));
    }

    public List<CinemaRoom> getCinemaRoomListByCinemaId(Integer cinemaId) {
        return cinemaRoomRepository.findByCinemaId(cinemaId);
    }

    public List<CinemaRoom> getCinemaRoomListByCityId(Integer cityId) {
        return cinemaRoomRepository.findByCityId(cityId);
    }

    public String showAllCinemasRooms() {
        return cinemaRoomRepository.findAll()
                .stream()
                .map(cinemaRoom -> cinemaRoom.getId() + ". " + cinemaRoom.getName())
                .collect(Collectors.joining("\n"));
    }

    public List<CinemaRoom> getAll() {
        return cinemaRoomRepository.findAll();
    }

    public List<CinemaRoom> getCinemaRoomListByMovieId(int movieId) {
        return cinemaRoomRepository.findByMovieId(movieId);
    }
}