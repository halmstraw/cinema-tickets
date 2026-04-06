package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {

    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        validateAccountId(accountId);
        validateRequests(ticketTypeRequests);
    }

    private void validateAccountId(Long accountId) {
        if (accountId <= 0L) throw new InvalidPurchaseException("Account Id is 0 or negative number");
    }

    private void validateRequests(TicketTypeRequest... ticketTypeRequests) {
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) throw new InvalidPurchaseException("TicketTypeRequest is null or empty");

        int totalTickets = 0, adultTickets = 0, childTickets = 0, infantTickets = 0;
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request == null) throw new InvalidPurchaseException("An individual ticket is null");
            if (request.getNoOfTickets() <= 0) throw new InvalidPurchaseException("No tickets found in request");
            totalTickets += request.getNoOfTickets();
            switch (request.getTicketType()) {
                case ADULT -> adultTickets += request.getNoOfTickets();
                case CHILD -> childTickets += request.getNoOfTickets();
                case INFANT -> infantTickets += request.getNoOfTickets();
            }
        }

        if (totalTickets > 25) throw new InvalidPurchaseException("Number of tickets exceeds maximum of 25");
        if ((childTickets > 0 || infantTickets > 0) && adultTickets == 0) throw new InvalidPurchaseException("No adult tickets purchased with infant or child");
        if (infantTickets > adultTickets) throw new InvalidPurchaseException("Infant tickets number exceeds adult tickets");
    }
}
