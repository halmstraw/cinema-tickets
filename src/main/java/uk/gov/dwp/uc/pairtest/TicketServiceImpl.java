package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketPurchaseValidator;

public class TicketServiceImpl implements TicketService {
    public static final int ADULT_TICKET_PRICE = 25;
    public static final int CHILD_TICKET_PRICE = 15;
    /**
     * Should only have private methods other than the one below.
     */
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;
    private final TicketPurchaseValidator validator;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService seatReservationService,TicketPurchaseValidator validator) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
        this.validator = validator;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validator.validate(accountId,ticketTypeRequests);

        int adultTickets = 0, childTickets = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            switch (request.getTicketType()) {
                case ADULT -> adultTickets += request.getNoOfTickets();
                case CHILD -> childTickets += request.getNoOfTickets();
            }
        }
        int totalAmountToPay = adultTickets * ADULT_TICKET_PRICE;
        totalAmountToPay += childTickets * CHILD_TICKET_PRICE;
        paymentService.makePayment(accountId, totalAmountToPay);

        int totalSeatsToAllocate = adultTickets;
        totalSeatsToAllocate += childTickets;
        seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);
    }
}
