package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.validation.TicketPurchaseValidator;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock private TicketPaymentService paymentService;

    private final TicketPurchaseValidator validator = new TicketPurchaseValidator();
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(paymentService, validator);
    }

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
}
