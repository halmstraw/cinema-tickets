package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock private TicketPaymentService paymentService;
    @InjectMocks private TicketServiceImpl ticketService;

    @Test
    void shouldCalculateCorrectPriceForSingleAdultTicket() {
        ticketService.purchaseTickets(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));

        verify(paymentService).makePayment(1L, 25);
    }
}
