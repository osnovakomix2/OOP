package core;

import java.time.LocalDateTime;
import java.util.Map;

public record UserReport(LocalDateTime time, Map<FuelType, Boolean> data, FuelStation station) {
    public UserReport {
        data = Map.copyOf(data);
    }
}
