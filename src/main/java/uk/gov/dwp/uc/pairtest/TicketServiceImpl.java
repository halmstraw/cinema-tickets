package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketPurchaseValidator;

import java.util.Objects;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private final TicketPaymentService paymentService;
    private final TicketPurchaseValidator validator;

    public TicketServiceImpl(TicketPaymentService paymentService, TicketPurchaseValidator validator) {
        this.paymentService = paymentService;
        this.validator = validator;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validator.validate(accountId,ticketTypeRequests);

        int adultTickets = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (Objects.requireNonNull(request.getTicketType()) == TicketTypeRequest.Type.ADULT) {
                adultTickets += request.getNoOfTickets();
            }
        }
        int ticketCost = adultTickets * 25;
        paymentService.makePayment(accountId,ticketCost);
    }

}
