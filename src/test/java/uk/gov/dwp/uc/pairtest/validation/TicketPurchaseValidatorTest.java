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
    @Test
    void shouldRejectPurchaseWhenAccountIdIsNull() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(null));
    }

    /* Input validation */
    @Test
    void shouldRejectPurchaseWhenTicketRequestArrayIsNull() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L, (TicketTypeRequest[]) null));
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
    @Test
    void shouldRejectPurchaseWhenAnyTicketTypeHasZeroQuantity() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT,0)));
    }

    /* Adult requirements */
    @Test
    void shouldRejectPurchaseWhenOnlyChildTicketsRequested() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1)));
    }
    @Test
    void shouldRejectPurchaseWhenOnlyInfantTicketsRequested() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1)));
    }
    @Test
    void shouldRejectPurchaseWhenChildAndInfantTicketsRequestedWithoutAdult() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1)
        ));
    }
    @Test
    void shouldAcceptPurchaseWhenChildTicketsRequestedWithAdult() {
        assertDoesNotThrow(() -> validator.validate(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1),
            new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1)
        ));
    }
    @Test
    void shouldAcceptPurchaseWhenInfantTicketsRequestedWithAdult() {
        assertDoesNotThrow(() -> validator.validate(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1)
        ));
    }

    /* Infant ratio test */
    @Test
    void shouldRejectPurchaseWhenMoreInfantsThanAdults() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT,2),
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1)
        ));
    }
    @Test
    void shouldAcceptPurchaseWhenInfantsEqualsAdults() {
        assertDoesNotThrow(() -> validator.validate(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1)
        ));
    }
    @Test
    void shouldAcceptPurchaseWhenInfantsFewerThanAdults() {
        assertDoesNotThrow(() -> validator.validate(1L,
            new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
            new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        ));
    }
    @Test
    void shouldRejectPurchaseWhenTicketTypeIsNull() {
        assertThrows(InvalidPurchaseException.class, () ->
            validator.validate(1L, new TicketTypeRequest(null, 1))
        );
    }
}
