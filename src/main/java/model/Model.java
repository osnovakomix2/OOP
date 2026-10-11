package model;

import core.District;
import core.FuelStation;
import core.FuelType;
import core.UserReport;
import storage.Storage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class Model {
    private final Storage stationStorage;
    public Model(Storage storage) {
        stationStorage = storage;
    }
    public List<FuelStation> findFuelInDistrict(District district, FuelType fuel) {
        return stationStorage.getStationsByDistrict(district).stream()
                .filter(x -> x.fuelTypes().contains(fuel)) //station where can be this fuel
                .filter(x -> {
                    var report = stationStorage.getLastReport(x); //can be null if station not reported
                    if(report == null) {
                        return false;
                    }
                    return report.data().getOrDefault(fuel, false);
                })
                .toList();
    }
    public List<FuelStation> findStationInDistrict(District district) {
        return stationStorage.getStationsByDistrict(district);
    }
    public void createReport(FuelStation station, Map<FuelType, Boolean> data) {
        stationStorage.saveReport(station, new UserReport(LocalDateTime.now(), data));
    }
}
