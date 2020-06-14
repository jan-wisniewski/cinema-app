package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.dto.CreateCinemaDto;
import wisniewski.jan.persistence.dto.CreateCinemaRoomDto;
import wisniewski.jan.persistence.mappers.Mapper;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.persistence.validator.CreateCinemaDtoValidator;
import wisniewski.jan.service.exception.AdminServiceException;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AdminService {

    private final CinemaRepository cinemaRepository;

    public Integer addCinema(CreateCinemaDto cinemaDto) {
        if (cinemaDto == null) {
            throw new AdminServiceException("Cinema dto is null");
        }
        var validator = new CreateCinemaDtoValidator();
        var errors = validator.validate(cinemaDto);
        if (validator.hasErrors()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add cinema errors:  " + errorsMessage);
        }

        //findByCinemaName -> jezeli jest to wyjątek, jezeli nie ma to idziemy dalej
        //1. createcineamdtonull
        //2. jak jest kino w db
        //3. prawidlowe wstawianie kina i sprawdzenie id

        if (cinemaRepository.findByName(cinemaDto.getName()).isPresent()) {
            throw new AdminServiceException("Cinema with this name is already on DB");
        }

        var cinema = Mapper.fromCinemaDtoToCinema(cinemaDto);
        var createdCinema = cinemaRepository
                .add(cinema)
                .orElseThrow(() -> new AdminServiceException("cannot insert to db"));
        return createdCinema.getId();
    }

}