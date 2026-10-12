package core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void toString_shouldReturnFormattedAddress() {
        Address address = new Address(District.KIROV, "Ленина", "10");
        assertEquals("ул. Ленина, д. 10", address.toString());
    }
}