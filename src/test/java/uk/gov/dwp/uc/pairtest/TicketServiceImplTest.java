package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketPurchaseValidator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock private TicketPaymentService paymentService;
    @Mock private SeatReservationService seatReservationService;

    private final TicketPurchaseValidator validator = new TicketPurchaseValidator();
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(paymentService, seatReservationService, validator);
    }

    /*Price Calculation*/
    @Test
    void shouldCalculateCorrectPriceForSingleAdultTicket() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));

        verify(paymentService).makePayment(1L, 25);
    }
    @Test
    void shouldCalculateCorrectPriceForSingleChildTicketWithAdult() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));

        verify(paymentService).makePayment(1L, 40);
    }
    @Test
    void shouldCalculateCorrectPriceForInfantTicketWithAdult() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));

        verify(paymentService).makePayment(1L, 25);
    }
    @Test
    void shouldCalculateCorrectPriceForMixedTickets() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));

        verify(paymentService).makePayment(1L, 80);
    }

    /*Seat Allocation*/
    @Test
    void shouldReserveOneSeatForSingleAdultTicket() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));

        verify(seatReservationService).reserveSeat(1L,1);
    }
    @Test
    void shouldReserveSeatsForAdultsAndChildrenButNotInfants() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));

        verify(seatReservationService).reserveSeat(1L, 4);
    }
    @Test
    void shouldReserveZeroSeatsForInfantOnly() {
        assertThrows(InvalidPurchaseException.class, () ->
            ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1))
        );

    }

    /*Happy Paths*/
    @Test
    void shouldProcessValidPurchaseOfOneAdultTicket() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));

        verify(paymentService).makePayment(1L, 25);
        verify(seatReservationService).reserveSeat(1L,1);
    }
    @Test
    void shouldProcessValidPurchaseOfMixedTickets() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));

        verify(paymentService).makePayment(1L, 80);
        verify(seatReservationService).reserveSeat(1L, 4);
    }
    @Test
    void shouldProcessValidPurchaseOfMixedTicketsAtMaxVolume() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5),
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20));

        verify(paymentService).makePayment(1L, 575);
        verify(seatReservationService).reserveSeat(1L, 25);
    }
}
