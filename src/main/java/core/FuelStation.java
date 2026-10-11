package core;

import java.util.List;
import java.util.stream.Collectors;

public record FuelStation(Address address, List<FuelType> fuelTypes, String brand, Integer id) {
    public FuelStation {
        fuelTypes = List.copyOf(fuelTypes);
    }
    @Override
    public String toString() {
        String fuels = fuelTypes.stream()
                .map(FuelType::toString)
                .collect(Collectors.joining(", "));
        return brand + " (" + address + ") \n"
                + "Продает топливо: " + fuels;
    }
}
