package core;

import java.util.Set;
import java.util.stream.Collectors;

public record FuelStation(Address address, Set<FuelType> fuelTypes, String brand, Integer id) {
    public FuelStation {
        fuelTypes = Set.copyOf(fuelTypes);
    }
    @Override
    public String toString() {
        String fuels = fuelTypes.stream()
                .map(FuelType::toString)
                .collect(Collectors.joining(", "));
        return "Заправка № " + id + ' ' + brand + address + '\n'
                + "Продает топливо:" + fuels;
    }
}
