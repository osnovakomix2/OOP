package parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.Address;
import core.District;
import core.FuelStation;
import core.FuelType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JsonStationParser implements StationParser {
    private final Map<String, FuelType> jsonFuelToEnum =
            Map.of(
            "fuel:octane_100", FuelType.AI_100,
            "fuel:octane_95", FuelType.AI_95,
            "fuel:octane_92", FuelType.AI_92,
            "fuel:diesel", FuelType.DT);

    @Override
    public List<FuelStation> parse(InputStream in) {
        InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        JsonObject data = JsonParser.parseReader(inReader).getAsJsonObject();
        var jsonStations = data.getAsJsonArray("features");

        var stations = new ArrayList<FuelStation>();
        var addresses = new HashSet<String>();
        int id = 1;

        for (var element : jsonStations) {
            var station = element.getAsJsonObject();
            if (!station.has("properties")) {
                continue;
            }

            var properties = station.getAsJsonObject("properties");
            String brand = getValue(properties, "name");
            String district = getValue(properties, "addr:district");
            String street = getValue(properties, "addr:street");
            String house = getValue(properties, "addr:housenumber");

            if(street.isBlank() || street.equals("Не указана") || district.isBlank() || district.equals("Не указан")){
                continue;
            }
            if(brand.isBlank()){
                brand = "АЗС";
            }

            String addressKey = (street + "_" + house).toLowerCase();
            if (addresses.contains(addressKey)) {
                continue;
            }
            addresses.add(addressKey);

            List<FuelType> fuels = new ArrayList<>();
            for (var entry : jsonFuelToEnum.entrySet()) {
                String jsonFuelKey = entry.getKey();
                if (properties.has(jsonFuelKey) && !properties.get(jsonFuelKey).isJsonNull()) {
                    if ("yes".equalsIgnoreCase(properties.get(jsonFuelKey).getAsString().trim())) {
                        fuels.add(entry.getValue());
                    }
                }
            }
            if (fuels.isEmpty()) {
                fuels = List.of(FuelType.AI_92, FuelType.AI_95, FuelType.DT);
            }

            District dist = getDistrict(district);
            if(dist == null) {
                continue;
            }

            FuelStation st = new FuelStation(new Address(dist, street, house), fuels, brand, id);
            id++;
            stations.add(st);
        }
        return stations;
    }

    private District getDistrict(String district) {
        if(district == null || district.isBlank()) {
            return null;
        }
        return switch (district.trim()) {
            case "Академический район", "Академический" -> District.AKADEM;
            case "Верх-Исетский район", "Верх-Исетский" -> District.ISETSKY;
            case "Железнодорожный район", "Железнодорожный" -> District.RAILWAY;
            case "Кировский район", "Кировский" -> District.KIROV;
            case "Ленинский район", "Ленинский" -> District.LENIN;
            case "Октябрьский район", "Октябрьский" -> District.OCTOBER;
            case "Орджоникидзевский район", "Орджоникидзевский" -> District.ORDZHONIKIDZE;
            case "Чкаловский район", "Чкаловский" -> District.CHKALOV;
            default -> null;
        };
    }

    private String getValue(JsonObject properties, String member) {
        if (properties.has(member) && !properties.get(member).isJsonNull()) {
            return properties.get(member).getAsString().trim();
        }
        return "";
    }
}