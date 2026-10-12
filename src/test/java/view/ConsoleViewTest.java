package view;

import core.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleViewTest {

    private ByteArrayOutputStream buffer;
    private PrintStream originalOut;
    private ConsoleView view;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        view = new ConsoleView();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String output() {
        return buffer.toString();
    }

    @Test
    void showGreeting_shouldPrintGreeting() {
        view.showGreeting();
        assertTrue(output().contains("Добро пожаловать"),
                "Вывод: " + output());
    }

    @Test
    void showMenu_shouldPrintAllOptions() {
        view.showMenu();
        String out = output();
        assertTrue(out.contains("0"), "Нет пункта 0: " + out);
        assertTrue(out.contains("1"), "Нет пункта 1: " + out);
        assertTrue(out.contains("-1"), "Нет выхода: " + out);
    }

    @Test
    void showMessage_shouldPrintMessage() {
        view.showMessage("Привет");
        assertTrue(output().contains("Привет"));
    }

    @Test
    void showDistricts_shouldPrintAllDistricts() {
        view.showDistricts(List.of(District.KIROV, District.LENIN));
        String out = output();
        assertTrue(out.contains("Кировский"), "Нет Кировского: " + out);
        assertTrue(out.contains("Ленинский"), "Нет Ленинского: " + out);
    }

    @Test
    void showFuelTypes_shouldPrintAllFuelTypes() {
        view.showFuelTypes(List.of(FuelType.AI_92, FuelType.DT));
        String out = output();
        assertTrue(out.contains("АИ-92"), "Нет АИ-92: " + out);
        assertTrue(out.contains("ДТ"), "Нет ДТ: " + out);
    }

    @Test
    void showStations_shouldPrintBrands() {
        FuelStation station = new FuelStation(
                new Address(District.KIROV, "Ленина", "1"),
                List.of(FuelType.AI_95),
                "Лукойл",
                1
        );
        view.showStations(List.of(station));
        assertTrue(output().contains("Лукойл"));
    }
}