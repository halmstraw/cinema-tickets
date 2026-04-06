package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {

    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateAccountId(accountId);
        validateRequests(ticketTypeRequests);
    }

    private void validateAccountId(Long accountId) {
        if (accountId <= 0L) throw new InvalidPurchaseException();
    }

    private void validateRequests(TicketTypeRequest... ticketTypeRequests) {
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) throw new InvalidPurchaseException();

        int totalTickets = 0, adultTickets = 0, childTickets = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request == null) throw new InvalidPurchaseException();
            if (request.getNoOfTickets() <= 0) throw new InvalidPurchaseException();
            totalTickets += request.getNoOfTickets();
            switch (request.getTicketType()) {
                case ADULT -> adultTickets += request.getNoOfTickets();
                case CHILD -> childTickets += request.getNoOfTickets();
            }
        }

        if (totalTickets > 25) throw new InvalidPurchaseException();
        if ((childTickets > 0) && adultTickets == 0) throw new InvalidPurchaseException();
    }
}
