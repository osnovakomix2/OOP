package controller;

import core.District;
import core.FuelType;
import model.Model;
import view.View;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public final class ConsoleController implements Controller{
    private final Model model;
    private final View view;
    private final Scanner scanner;
    private final List<District> districts = List.of(District.values());
    private final List<FuelType> fuelTypes = List.of(FuelType.values());

    public ConsoleController(Model md, View vm) {
        this(md, vm, System.in);
    }

    public ConsoleController(Model md, View vm, InputStream in) {
        model = md;
        view = vm;
        scanner = new Scanner(in);
    }
    private int getIntSafety() {
        while (!scanner.hasNextInt()){
            view.showMessage("Пожалуйста, введите число");
            scanner.next();
        }
        return scanner.nextInt();
    }
    private int readIntRange(int min, int max) {
        int userInput = getIntSafety();
        while(userInput < min || userInput > max) {
            view.showMessage(String.format("Пожалуйста, введите число от %d до %d", min, max));
            userInput = getIntSafety();
        }
        return userInput;
    }
    private void searchingFuel() {
        var district = selectDistrict();
        view.showMessage("Выберите тип топлива: ");
        view.showFuelTypes(fuelTypes);
        FuelType fuel = fuelTypes.get(readIntRange(0, fuelTypes.size() - 1));
        var searchedStation = model.findFuelInDistrict(district, fuel);
        if(searchedStation.isEmpty()) {
            view.showMessage("К сожалению, в районе " + district + " заправки с топливом " + fuel + " не найдены.");
            view.showMessage("Возможно, топливо отсутствует или нет актуальной информации");
            sayTaskEnd();
            return;
        }
        view.showMessage("В районе " + district + " найдены заправки с топливом " + fuel);
        view.showStations(searchedStation);
        sayTaskEnd();
    }
    private void makingReport() {
        var district = selectDistrict();
        view.showMessage("Выберете АЗС: ");
        var st = model.findStationInDistrict(district);
        view.showStations(st);
        var station = st.get(readIntRange(0, st.size() - 1));
        var userData = new HashMap<FuelType, Boolean>();
        station.fuelTypes().stream()
                .forEach(x -> {
                    view.showMessage("В наличии ли на данной заправке " + x + " ? (1 - Да, 0 - Нет)");
                    int wasFuel = readIntRange(0, 1);
                    if(wasFuel == 0) {
                        userData.put(x, Boolean.FALSE);
                    }
                    else{
                        userData.put(x, Boolean.TRUE);
                    }
                });
        model.createReport(station, userData);
        sayTaskEnd();
    }
    private District selectDistrict() {
        view.showMessage("Выберете район: ");
        view.showDistricts(districts);
        int choice = readIntRange(0, districts.size() - 1);
        return districts.get(choice);
    }
    private void sayTaskEnd() {
        view.showMessage("Задача выполнена");
        view.showMenu();
    }
    @Override
    public void start() {
        view.showGreeting();
        view.showMenu();
        boolean working = true;
        while(working) {
            int userInput = readIntRange(-1, 2);
            switch (userInput) {
                case 0 -> view.showInfo();
                case 1 -> searchingFuel();
                case 2 -> makingReport();
                case -1 -> working = false;
            }
        }
    }
}
