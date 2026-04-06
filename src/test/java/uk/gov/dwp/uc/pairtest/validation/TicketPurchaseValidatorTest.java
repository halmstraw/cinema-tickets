package uk.gov.dwp.uc.pairtest.validation;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketPurchaseValidatorTest {

    private final TicketPurchaseValidator validator = new TicketPurchaseValidator();

    /* Account Validation */
    @Test
    void shouldRejectPurchaseWhenAccountIDIsZero() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(0L));
    }
    @Test
    void shouldRejectPurchaseWhenAccountIDIsNegative() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(-1L));
    }
    @Test
    void shouldAcceptPurchaseWhenAccountIDIsPositive() {
        assertDoesNotThrow(() -> validator.validate(1L,new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1)));
    }

    /* Input validation */
    @Test
    void shouldRejectPurchaseWhenTicketRequestArrayIsNull() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L,null));
    }
    @Test
    void shouldRejectPurchaseWhenTicketRequestArrayIsEmpty() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L));
    }
    @Test
    void shouldRejectPurchaseWhenAnyIndividualTicketRequestIsNull() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L, (TicketTypeRequest) null));
    }

    /* Ticket quantity validation */
    @Test
    void shouldRejectPurchaseWhenTotalTicketsExceeds25() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,26)));
    }
    @Test
    void shouldAcceptPurchaseWhenTotalTicketsEquals25() {
        assertDoesNotThrow(() -> validator.validate(1L,new TicketTypeRequest(TicketTypeRequest.Type.ADULT,25)));
    }
    @Test
    void shouldRejectPurchaseWhenAnyTicketTypeHasNegativeQuantity() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,-1)));
    }

}
