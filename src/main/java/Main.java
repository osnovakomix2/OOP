import controller.ConsoleController;
import controller.Controller;
import model.Model;
import parser.JsonStationParser;
import storage.FuelStationStorage;
import storage.Storage;
import view.ConsoleView;
import view.View;

import java.nio.file.Path;


public class Main {
    public static void main(String[] args) {
        String path = "src/main/resources/fuelEkbFinal.geojson";
        JsonStationParser parser = new JsonStationParser();
        var stations = parser.parse(Path.of(path));

        Storage storage = new FuelStationStorage(stations);
        Model model = new Model(storage);
        View view = new ConsoleView();
        Controller controller = new ConsoleController(model, view);

        controller.start();
    }
}
