package uk.gov.dwp.uc.pairtest.validation;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketPurchaseValidatorTest {

    private final TicketPurchaseValidator validator = new TicketPurchaseValidator();

    @Test
    void shouldRejectPurchaseWhenAccountIDIsZero() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(0L));
    }

    @Test
    void shouldRejectPurchaseWhenAccountIDIsNegative() {
        assertThrows(InvalidPurchaseException.class, () -> validator.validate(-1L));
    }

}
