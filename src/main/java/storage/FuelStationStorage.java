package storage;

import core.District;
import core.FuelStation;
import core.UserReport;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FuelStationStorage implements Storage{
    ArrayList<FuelStation> stations;
    HashMap<FuelStation, ArrayDeque<UserReport>> reportsByStation;
    FuelStationStorage(List<FuelStation> cityStations) {
        //дописать
        //stations = (ArrayList<FuelStation>) List.copyOf(cityStations);
        //reportsByStation = new HashMap<FuelStation, ArrayDeque<UserReport>>();
        //reportsByStation.stream().
    }
    @Override
    public List<FuelStation> getAllStations() {
        return List.of();
    }

    @Override
    public List<FuelStation> getStationsByDistrict(District district) {
        return List.of();
    }

    @Override
    public FuelStation getStationById(int id) {
        return null;
    }

    @Override
    public void saveReport(UserReport report) {

    }

    @Override
    public UserReport getLastReport(FuelStation station) {
        return null;
    }
}
