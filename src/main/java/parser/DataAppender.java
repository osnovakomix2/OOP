package parser;

import com.google.gson.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;

public final class DataAppender {
    private final String in = "src/main/resources/fuelEKB12.10.geojson";
    private final String out = "src/main/resources/fuelEkbFinal.geojson";

    private record Address(String district, String street, String house) {}

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        new DataAppender().run();
    }

    private void run() {
        try {
            var data = JsonParser.parseString(Files.readString(Path.of(in), StandardCharsets.UTF_8)).getAsJsonObject();
            var jsonStations = data.getAsJsonArray("features");
            System.out.println("Заправок в файле: " + jsonStations.size());
            //Check .geojson We have a Json array of stations
            for(int i = 0; i < jsonStations.size(); ++i) {
                JsonObject jsonStation = jsonStations.get(i).getAsJsonObject();
                JsonObject jsonProperties = jsonStation.getAsJsonObject("properties");
                JsonObject jsonPoint = jsonStation.getAsJsonObject("geometry");
                //Objects parsed from json
                boolean hasStreet = jsonProperties.has("addr:street");
                boolean hasHouse = jsonProperties.has("addr:housenumber");
                boolean hasDistrict = jsonProperties.has("addr:district");
                JsonArray coords = jsonPoint.getAsJsonArray("coordinates");
                double lon = coords.get(0).getAsDouble();
                double lat = coords.get(1).getAsDouble();

                var addressByPoint = requestAddress(lon, lat);

                if(!hasDistrict && addressByPoint.district != null) {
                    jsonProperties.addProperty("addr:district", addressByPoint.district);
                }
                if(!hasStreet && addressByPoint.street != null) {
                    jsonProperties.addProperty("addr:street", addressByPoint.street);
                }
                if(!hasHouse && addressByPoint.house != null) {
                    jsonProperties.addProperty("addr:housenumber", addressByPoint.house);
                }
                String name = jsonProperties.has("name") ? jsonProperties.get("name").getAsString() : "АЗС";
                String log = String.format("[%d/%d] Дополнен адрес: %s -> %s, %s (%s)",
                        (i + 1), jsonStations.size(), name,
                        jsonProperties.get("addr:street"),
                        jsonProperties.get("addr:housenumber"),
                        jsonProperties.get("addr:district"));
                System.out.println(log);
                //Don`t delete!!! Website requirement
                Thread.sleep(1000);
            }
            Files.writeString(Path.of(out), gson.toJson(data));

        } catch (Exception e) {
            System.err.print(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Address requestAddress(double lon, double lat){
        String url = String.format(Locale.US,
                "https://nominatim.openstreetmap.org/reverse?lat=%.6f&lon=%.6f&format=jsonv2&accept-language=ru",
                lat, lon);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "EkbStationsGeoJson/1.0")
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                if (json.has("address")) {
                    JsonObject addr = json.getAsJsonObject("address");
                    String district = getField(addr, "city_district",
                            getField(addr, "suburb",
                                    getField(addr, "district", "Не указан")));
                    String street = getField(addr, "road", "Не указана");
                    String house = getField(addr, "house_number", "-");
                    return new Address(district, street, house);
                }
            }
        } catch (Exception e) {}
        return new Address("Не указан", "Не указана", "-");
    }

    private String getField(JsonObject obj, String key, String defaultVal) {
        if(obj.has(key) && !obj.get(key).isJsonNull()){
            return obj.get(key).getAsString();
        }
        return defaultVal;
    }
}
