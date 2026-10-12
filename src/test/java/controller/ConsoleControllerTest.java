package controller;

import core.*;
import model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.FuelStationStorage;
import view.View;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleControllerTest {

    static class FakeView implements View {
        final List<String> messages = new ArrayList<>();
        final List<List<FuelStation>> shownStations = new ArrayList<>();
        int greetingCount = 0;
        int menuCount = 0;
        int infoCount = 0;

        @Override public void showGreeting() { greetingCount++; }
        @Override public void showMenu()     { menuCount++; }
        @Override public void showInfo()     { infoCount++; }

        @Override
        public void showMessage(String message) {
            messages.add(message);
        }

        @Override
        public void showStations(List<FuelStation> stations) {
            shownStations.add(stations);
        }

        @Override public void showDistricts(List<District> districts) { /* ignore */ }
        @Override public void showFuelTypes(List<FuelType> fuelTypes)  { /* ignore */ }
    }

    private FakeView view;
    private Model model;
    private FuelStationStorage storage;
    private FuelStation station;

    @BeforeEach
    void setUp() {
        District district = District.values()[0];

        station = new FuelStation(
                new Address(district, "Ленина", "1"),
                List.of(FuelType.AI_95),
                "Лукойл",
                1
        );

        storage = new FuelStationStorage(List.of(station));

        storage.saveReport(station,
                new UserReport(LocalDateTime.now(), Map.of(FuelType.AI_95, true)));

        model = new Model(storage);
        view = new FakeView();
    }

    private ConsoleController controllerWithInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        return new ConsoleController(model, view, in);
    }

    @Test
    void start_shouldShowGreetingAndMenu() {
        controllerWithInput("-1\n").start();

        assertEquals(1, view.greetingCount, "Приветствие должно быть показано один раз");
        assertTrue(view.menuCount >= 1, "Меню должно быть показано хотя бы раз");
    }

    @Test
    void start_whenUserSelectsInfo_shouldCallShowInfo() {
        controllerWithInput("0\n-1\n").start();

        assertEquals(1, view.infoCount, "Справка должна быть показана один раз");
    }

    @Test
    void start_whenUserSearchesFuel_shouldShowFoundStations() {
        int districtIdx = 0;
        int fuelIdx = List.of(FuelType.values()).indexOf(FuelType.AI_95);

        String input = "1\n" + districtIdx + "\n" + fuelIdx + "\n-1\n";
        controllerWithInput(input).start();

        assertFalse(view.shownStations.isEmpty(),
                "showStations не был вызван. Сообщения: " + view.messages);

        boolean hasStation = view.shownStations.stream()
                .anyMatch(list -> list.stream().anyMatch(s -> s.equals(station)));
        assertTrue(hasStation,
                "Наша станция не появилась в результатах поиска. " +
                        "shownStations=" + view.shownStations.size() +
                        ", messages=" + view.messages);
    }

    @Test
    void start_whenUserMakesReport_shouldSaveReport() {
        int fuelCount = station.fuelTypes().size();

        StringBuilder sb = new StringBuilder();
        sb.append("2\n");
        sb.append("0\n");
        sb.append("0\n");
        for (int i = 0; i < fuelCount; i++) {
            sb.append("1\n");
        }
        sb.append("-1\n");

        controllerWithInput(sb.toString()).start();

        UserReport report = storage.getLastReport(station);
        assertNotNull(report, "Отчёт не был сохранён. Сообщения: " + view.messages);
    }

    @Test
    void start_whenUserEntersNonInteger_shouldShowError() {
        controllerWithInput("abc\n0\n-1\n").start();

        boolean hasError = view.messages.stream()
                .anyMatch(m -> m.toLowerCase().contains("число"));
        assertTrue(hasError, "Сообщение о вводе числа не найдено: " + view.messages);
    }

    @Test
    void start_whenUserEntersOutOfRange_shouldShowError() {
        controllerWithInput("99\n0\n-1\n").start();

        boolean hasRangeError = view.messages.stream()
                .anyMatch(m -> m.toLowerCase().contains("число от"));
        assertTrue(hasRangeError,
                "Сообщение о неверном диапазоне не найдено: " + view.messages);
    }
}