package core;

import java.util.Set;

public record FuelStation(Address address, Set<FuelType> fuelTypes, String brand, int id) {
    public FuelStation {
        fuelTypes = Set.copyOf(fuelTypes);
    }
}
