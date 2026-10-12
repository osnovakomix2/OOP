package model;

import core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.FuelStationStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    private FuelStation withFuel;
    private FuelStation withoutFuel;
    private FuelStation noReport;
    private FuelStationStorage storage;
    private Model model;

    @BeforeEach
    void setUp() {
        withFuel = new FuelStation(
                new Address(District.KIROV, "Ленина", "1"),
                List.of(FuelType.AI_95),
                "Лукойл",
                1
        );
        withoutFuel = new FuelStation(
                new Address(District.KIROV, "Мира", "2"),
                List.of(FuelType.AI_95),
                "Газпром",
                2
        );
        noReport = new FuelStation(
                new Address(District.KIROV, "Свободы", "3"),
                List.of(FuelType.AI_95),
                "Роснефть",
                3
        );

        storage = new FuelStationStorage(List.of(withFuel, withoutFuel, noReport));
        storage.saveReport(withFuel,
                new UserReport(LocalDateTime.now(), Map.of(FuelType.AI_95, true)));
        storage.saveReport(withoutFuel,
                new UserReport(LocalDateTime.now(), Map.of(FuelType.AI_95, false)));

        model = new Model(storage);
    }

    @Test
    void findFuelInDistrict_shouldReturnOnlyStationsWithFuel() {
        List<FuelStation> result = model.findFuelInDistrict(District.KIROV, FuelType.AI_95);

        assertEquals(1, result.size());
        assertEquals(withFuel, result.get(0));
    }

    @Test
    void findFuelInDistrict_shouldIgnoreStationsWithoutReports() {
        List<FuelStation> result = model.findFuelInDistrict(District.KIROV, FuelType.AI_95);

        assertFalse(result.contains(noReport));
        assertFalse(result.contains(withoutFuel));
    }

    @Test
    void findFuelInDistrict_shouldReturnEmptyForUnknownDistrict() {
        List<FuelStation> result = model.findFuelInDistrict(District.CHKALOV, FuelType.AI_95);
        assertTrue(result.isEmpty());
    }

    @Test
    void findFuelInDistrict_shouldReturnEmptyWhenNoStationHasFuel() {
        List<FuelStation> result = model.findFuelInDistrict(District.KIROV, FuelType.AI_100);
        assertTrue(result.isEmpty());
    }

    @Test
    void findStationInDistrict_shouldReturnAllStationsOfDistrict() {
        List<FuelStation> result = model.findStationInDistrict(District.KIROV);

        assertEquals(3, result.size());
        assertTrue(result.contains(withFuel));
        assertTrue(result.contains(withoutFuel));
        assertTrue(result.contains(noReport));
    }

    @Test
    void createReport_shouldSaveReportToStorage() {
        Map<FuelType, Boolean> data = Map.of(FuelType.AI_95, true);
        model.createReport(noReport, data);

        UserReport saved = storage.getLastReport(noReport);
        assertNotNull(saved, "Отчёт должен быть сохранён");
        assertEquals(Boolean.TRUE, saved.data().get(FuelType.AI_95));
    }

    @Test
    void createReport_shouldBeVisibleInFindFuel() {
        List<FuelStation> before = model.findFuelInDistrict(District.KIROV, FuelType.AI_95);
        assertFalse(before.contains(noReport));

        model.createReport(noReport, Map.of(FuelType.AI_95, true));

        List<FuelStation> after = model.findFuelInDistrict(District.KIROV, FuelType.AI_95);
        assertTrue(after.contains(noReport));
    }
}