package storage;

import core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FuelStationStorageTest {

    private FuelStation station1;
    private FuelStation station2;
    private FuelStationStorage storage;

    @BeforeEach
    void setUp() {
        station1 = new FuelStation(
                new Address(District.KIROV, "Ленина", "1"),
                List.of(FuelType.AI_92, FuelType.AI_95),
                "Лукойл",
                1
        );
        station2 = new FuelStation(
                new Address(District.KIROV, "Мира", "2"),
                List.of(FuelType.DT),
                "Газпром",
                2
        );
        storage = new FuelStationStorage(List.of(station1, station2));
    }

    @Test
    void constructor_shouldThrowOnDuplicateIds() {
        FuelStation duplicate = new FuelStation(
                new Address(District.LENIN, "Тест", "3"),
                List.of(FuelType.AI_100),
                "Тест",
                1
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                new FuelStationStorage(List.of(station1, duplicate))
        );
        assertEquals("Duplicates have been detected in the data", ex.getMessage());
    }

    @Test
    void getAllStations_shouldReturnAllStations() {
        List<FuelStation> all = storage.getAllStations();
        assertEquals(2, all.size());
        assertTrue(all.contains(station1));
        assertTrue(all.contains(station2));
    }

    @Test
    void getStationsByDistrict_shouldFilterCorrectly() {
        FuelStation otherDistrict = new FuelStation(
                new Address(District.LENIN, "Тест", "4"),
                List.of(FuelType.DT),
                "Тест",
                3
        );
        storage = new FuelStationStorage(List.of(station1, station2, otherDistrict));

        List<FuelStation> kirov = storage.getStationsByDistrict(District.KIROV);
        assertEquals(2, kirov.size());
        assertTrue(kirov.contains(station1));
        assertTrue(kirov.contains(station2));

        List<FuelStation> lenin = storage.getStationsByDistrict(District.LENIN);
        assertEquals(1, lenin.size());
        assertTrue(lenin.contains(otherDistrict));
    }

    @Test
    void getStationsByDistrict_shouldReturnEmptyForUnknownDistrict() {
        List<FuelStation> result = storage.getStationsByDistrict(District.CHKALOV);
        assertTrue(result.isEmpty());
    }

    @Test
    void getStationById_shouldReturnStationOrNull() {
        assertEquals(station1, storage.getStationById(1));
        assertEquals(station2, storage.getStationById(2));
        assertNull(storage.getStationById(999));
    }

    @Test
    void saveReport_shouldStoreReportForStation() {
        UserReport report = new UserReport(
                LocalDateTime.now(),
                Map.of(FuelType.AI_92, true)
        );
        storage.saveReport(station1, report);

        assertEquals(report, storage.getLastReport(station1));
    }

    @Test
    void saveReport_shouldIgnoreUnknownStation() {
        FuelStation unknown = new FuelStation(
                new Address(District.CHKALOV, "Неизвестная", "0"),
                List.of(FuelType.DT),
                "Unknown",
                999
        );
        UserReport report = new UserReport(
                LocalDateTime.now(),
                Map.of(FuelType.DT, true)
        );

        assertDoesNotThrow(() -> storage.saveReport(unknown, report));
        assertNull(storage.getLastReport(unknown));
    }

    @Test
    void getLastReport_shouldReturnNullWhenNoReports() {
        assertNull(storage.getLastReport(station1));
    }

    @Test
    void getLastReport_shouldReturnLatestReport() {
        UserReport first = new UserReport(
                LocalDateTime.now().minusHours(1),
                Map.of(FuelType.AI_92, false)
        );
        UserReport second = new UserReport(
                LocalDateTime.now(),
                Map.of(FuelType.AI_92, true)
        );
        storage.saveReport(station1, first);
        storage.saveReport(station1, second);

        assertEquals(second, storage.getLastReport(station1));
    }
}