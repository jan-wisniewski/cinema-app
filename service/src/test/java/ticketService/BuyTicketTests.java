package ticketService;

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
import wisniewski.jan.persistence.dto.CreateTicketDto;
import wisniewski.jan.persistence.mappers.Mapper;
import wisniewski.jan.persistence.model.Ticket;
import wisniewski.jan.persistence.repository.TicketRepository;
import wisniewski.jan.service.TicketService;
import wisniewski.jan.service.exception.TicketServiceException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(LoggerExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BuyTicketTests {

    private Logger logger;

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Test
    @DisplayName("when ticketDto is null exception thrown")
    public void test1() {
        assertThrows(TicketServiceException.class, () -> ticketService.buyTicket(null));
        logger.info("Failed to create ticket: ticketDto is null");
    }

    @Test
    @DisplayName("when discount is negative exception thrown")
    public void test5() {
        CreateTicketDto ticketDto = CreateTicketDto
                .builder()
                .discount(BigDecimal.valueOf(-1))
                .price(BigDecimal.TEN)
                .seanceId(1)
                .userId(2)
                .seatId(2)
                .build();
        var ticket = Mapper.fromCreateTicketDtoToTicket(ticketDto);
        var expected = Ticket
                .builder()
                .discount(BigDecimal.valueOf(-1))
                .price(BigDecimal.TEN)
                .seanceId(1)
                .userId(2)
                .seatId(2)
                .build();
        Mockito
                .when(ticketRepository.add(ticket))
                .thenReturn(Optional.of(expected));
        assertThrows(TicketServiceException.class, () -> ticketService.buyTicket(ticketDto));
        logger.info("Failed to create ticket: discount is negative");
    }

    @Test
    @DisplayName("when price is negative exception thrown")
    public void test6() {
        CreateTicketDto ticketDto = CreateTicketDto
                .builder()
                .discount(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(-2))
                .seanceId(1)
                .userId(2)
                .seatId(2)
                .build();
        var ticket = Mapper.fromCreateTicketDtoToTicket(ticketDto);
        var expected = Ticket
                .builder()
                .discount(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(-2))
                .seanceId(1)
                .userId(2)
                .seatId(2)
                .build();
        Mockito
                .when(ticketRepository.add(ticket))
                .thenReturn(Optional.of(expected));
        assertThrows(TicketServiceException.class, () -> ticketService.buyTicket(ticketDto));
        logger.info("Failed to create ticket: price is negative");
    }

    @Test
    @DisplayName("when ticket data is correct return ticket id")
    public void test7() {
        CreateTicketDto ticketDto = CreateTicketDto
                .builder()
                .discount(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(2))
                .seanceId(1)
                .userId(2)
                .seatId(2)
                .build();
        var ticket = Mapper.fromCreateTicketDtoToTicket(ticketDto);
        var expected = Ticket
                .builder()
                .id(1)
                .discount(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(2))
                .seanceId(1)
                .userId(2)
                .seatId(2)
                .build();
        Mockito
                .when(ticketRepository.add(ticket))
                .thenReturn(Optional.of(expected));

        Mockito
                .when(ticketRepository.isPlaceAvailable(ticket))
                .thenReturn(true);

        assertEquals(expected.getId(), ticketService.buyTicket(ticketDto));
        logger.info("Ticket created successfully ");
    }

    @Test
    @DisplayName("if place is not available exception has been thrown")
    public void test8() {
        var ticketDto = CreateTicketDto
                .builder()
                .discount(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(2))
                .seanceId(1)
                .userId(2)
                .seatId(2)
                .build();

        var ticket = Mapper.fromCreateTicketDtoToTicket(ticketDto);

        Mockito
                .when(ticketRepository.isPlaceAvailable(ticket))
                .thenReturn(false);

        String exceptionMessage = "";
        try {
            ticketService.buyTicket(ticketDto);
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("This place is not available", exceptionMessage);
    }
}
