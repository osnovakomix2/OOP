package core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DistrictTest {

    @Test
    void toString_shouldReturnRussianName() {
        assertEquals("Академический", District.AKADEM.toString());
        assertEquals("Октябрьский", District.OCTOBER.toString());
        assertEquals("Кировский", District.KIROV.toString());
    }
}