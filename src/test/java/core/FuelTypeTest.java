package core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FuelTypeTest {

    @Test
    void toString_shouldReturnDisplayName() {
        assertEquals("АИ-95", FuelType.AI_95.toString());
        assertEquals("ДТ", FuelType.DT.toString());
        assertEquals("АИ-92", FuelType.AI_92.toString());
    }
}