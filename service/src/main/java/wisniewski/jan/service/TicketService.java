package wisniewski.jan.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.mappers.Mapper;
import wisniewski.jan.persistence.repository.TicketRepository;
import wisniewski.jan.persistence.validator.CreateTicketDtoValidator;
import wisniewski.jan.service.exception.TicketServiceException;
import wisniewski.jan.service.exception.UserServiceException;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Integer buyTicket(CreateTicketDto ticketDto) {
        if (Objects.isNull(ticketDto)) {
            throw new TicketServiceException("Ticket Dto is null");
        }
        var ticketValidator = new CreateTicketDtoValidator();
        var errors = ticketValidator.validate(ticketDto);
        if (ticketValidator.hasErrors()) {
            var messageErrors = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getValue() + " : " + e.getKey())
                    .collect(Collectors.joining("\n"));
            throw new TicketServiceException("Create ticket validation errors: " + messageErrors);
        }

        var ticket = Mapper.fromCreateTicketDtoToTicket(ticketDto);
        var createdTicket = ticketRepository
                .add(ticket)
                .orElseThrow(() -> new UserServiceException("cannot insert user to db"));
        return createdTicket.getId();
    }



}