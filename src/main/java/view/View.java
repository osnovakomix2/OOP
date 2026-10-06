package view;

import core.District;
import core.FuelStation;
import core.FuelType;

import java.util.List;
import java.util.Set;

public interface View {
    void showMessage(String data);
    void showStations(List<FuelStation> stations);
    void showDistricts(List<District> districts);
    void showFuelTypes(Set<FuelType> fuel);
    void showInfo();
    void showGreeting();
}
