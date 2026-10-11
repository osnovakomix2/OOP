package storage;

import core.District;
import core.FuelStation;
import core.UserReport;

import java.util.List;

public interface Storage {
    List<FuelStation> getAllStations();
    List<FuelStation> getStationsByDistrict(District district);
    FuelStation getStationById(int id);
    void saveReport(FuelStation station, UserReport report);
    UserReport getLastReport(FuelStation station);
}
