package core;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FuelStationTest {

    @Test
    void constructor_shouldCopyFuelTypesList() {
        List<FuelType> fuels = new ArrayList<>();
        fuels.add(FuelType.AI_92);
        fuels.add(FuelType.AI_95);

        Address address = new Address(District.LENIN, "Мира", "1");
        FuelStation station = new FuelStation(address, fuels, "Лукойл", 1);

        fuels.add(FuelType.DT);

        assertEquals(2, station.fuelTypes().size());
        assertTrue(station.fuelTypes().contains(FuelType.AI_92));
        assertTrue(station.fuelTypes().contains(FuelType.AI_95));
        assertFalse(station.fuelTypes().contains(FuelType.DT));
    }

    @Test
    void toString_shouldContainBrandAddressAndFuels() {
        Address address = new Address(District.KIROV, "Свободы", "5");
        FuelStation station = new FuelStation(
                address,
                List.of(FuelType.AI_100, FuelType.DT),
                "Газпром",
                42
        );

        String result = station.toString();
        assertTrue(result.contains("Газпром"), "Нет бренда в строке: " + result);
        assertTrue(result.contains("Свободы"), "Нет улицы в строке: " + result);
        assertTrue(result.contains("АИ-100"), "Нет топлива АИ-100: " + result);
        assertTrue(result.contains("ДТ"), "Нет топлива ДТ: " + result);
    }
}