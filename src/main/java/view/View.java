package view;

import core.District;
import core.FuelStation;
import core.FuelType;

import java.util.List;

public interface View {
    void showMessage(String data);
    void showStations(List<FuelStation> stations);
    void showDistricts(List<District> districts);
    void showFuelTypes(List<FuelType> fuel);
    void showInfo();
    void showMenu();
    void showGreeting();
}
