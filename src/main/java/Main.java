import controller.ConsoleController;
import controller.Controller;
import core.Address;
import core.District;
import core.FuelStation;
import core.FuelType;
import model.Model;
import storage.FuelStationStorage;
import storage.Storage;
import view.ConsoleView;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //in this will be parser
        ArrayList<FuelStation> example = new ArrayList<FuelStation>();
        example.add(new FuelStation(new Address(District.AKADEM, "Академическая", "12А"), List.of(FuelType.values()), "ЛУКОЙЛ", 0));
        example.add(new FuelStation(new Address(District.AKADEM, "Ясная", "1"), List.of(FuelType.values()), "Газпромнефть", 1));
        //after implement parser delete this!!

        Storage storage = new FuelStationStorage(example);
        Model model = new Model(storage);
        View view = new ConsoleView();
        Controller controller = new ConsoleController(model, view);
        controller.start();
    }
}
