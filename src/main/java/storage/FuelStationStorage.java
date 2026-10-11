package storage;

import core.District;
import core.FuelStation;
import core.UserReport;

import java.util.*;

public final class  FuelStationStorage implements Storage{
    private final List<FuelStation> stations;
    private final Map<Integer, FuelStation> stationsById;
    private final Map<Integer, ArrayDeque<UserReport>> reportsByStationId;

    public FuelStationStorage(List<FuelStation> cityStations) {
        stations = List.copyOf(cityStations);
        stationsById = new HashMap<Integer, FuelStation>();
        reportsByStationId = new HashMap<Integer, ArrayDeque<UserReport>>();
        for(var st : stations) {
            if(stationsById.containsKey(st.id())) {
                throw new RuntimeException("Duplicates have been detected in the data");
            }
            reportsByStationId.put(st.id(), new ArrayDeque<UserReport>());
            stationsById.put(st.id(), st);
        }
    }
    @Override
    public List<FuelStation> getAllStations() {
        return stations;
    }

    @Override
    public List<FuelStation> getStationsByDistrict(District district) {
        return stations.stream()
                .filter(x -> x.address().district() == district)
                .toList();
    }

    @Override
    public FuelStation getStationById(int id) {
        return stationsById.get(id);
    }

    @Override
    public void saveReport(FuelStation station, UserReport report) {
        var reports = reportsByStationId.get(station.id());
        if (reports != null) {
            reports.add(report);
        }
    }

    @Override
    public UserReport getLastReport(FuelStation station) {
        if(!reportsByStationId.containsKey(station.id())) {
            return null;
        }
        var reports = reportsByStationId.get(station.id());
        if(reports.isEmpty()) {
            return null;
        }
        return reports.getLast();
    }
}
