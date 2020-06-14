package wisniewski.jan.persistence.mappers;

import wisniewski.jan.persistence.dto.CreateCinemaDto;
import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.dto.CreateUserDto;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.model.Ticket;
import wisniewski.jan.persistence.model.User;

public interface Mapper {
    static User fromCreateUserDtoToUser(CreateUserDto userDto) {
        return User
                .builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }

    static Ticket fromCreateTicketDtoToTicket(CreateTicketDto ticketDto) {
        return Ticket
                .builder()
                .discount(ticketDto.getDiscount())
                .price(ticketDto.getPrice())
                .seanceId(ticketDto.getSeanceId())
                .seatId(ticketDto.getSeatId())
                .userId(ticketDto.getUserId())
                .build();
    }

    static Cinema fromCinemaDtoToCinema (CreateCinemaDto cinemaDto){
        return Cinema
                .builder()
                .cityId(cinemaDto.getCityId())
                .name(cinemaDto.getName())
                .build();
    }

}
