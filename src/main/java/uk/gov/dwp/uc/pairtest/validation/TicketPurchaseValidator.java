package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {

    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId <= 0L) {
            throw new InvalidPurchaseException();
        }

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException();
        }

        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request == null) {
                throw new InvalidPurchaseException();
            }
        }
    }

}
