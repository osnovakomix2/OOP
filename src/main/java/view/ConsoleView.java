package view;

import core.District;
import core.FuelStation;
import core.FuelType;

import java.io.PrintStream;
import java.util.List;

public final class ConsoleView implements View {
    private final PrintStream out;
    public ConsoleView() {
        this(System.out);
    }
    private <T> void showCollection(List<T> collection) {
        for(int i = 0; i < collection.size(); ++i) {
            var ou = String.format("%d) ", i) + collection.get(i).toString();
            out.println(ou);
        }
    }
    public ConsoleView(PrintStream o) {
        out = o;
    }
    @Override
    public void showMessage(String data) {
        out.println(data);
    }

    @Override
    public void showStations(List<FuelStation> stations) {
        showCollection(stations);
    }

    @Override
    public void showDistricts(List<District> districts) {
        showCollection(districts);
    }

    @Override
    public void showFuelTypes(List<FuelType> fuel) {
        showCollection(fuel);
    }

    @Override
    public void showInfo() {
        String info = """
                Бот умеет находить заправки по району с топливом в наличии.
                Для этого выберете 1.
                Затем выберете нужный район. Бот выведет заправки, где люди сообщили о наличии топлива.
                
                Бот умеет фиксировать сообщения о конкретных АЗС.
                Для этого выберете 2. Затем выберете нужный район и заправку.
                Ответьте "1" если данный вид топлива в наличии, "0" если нет.
                
                -1 для выхода
                """;
        out.println(info);
    }

    @Override
    public void showMenu() {
        String menu = """
                Введите цифру нужного действия
                 0 Вызвать справку
                 1 Проверить наличие топлива в районе
                 2 Сообщить о ситуации на АЗС
                -1 Выйти
                """;
        out.println(menu);
    }

    @Override
    public void showGreeting() {
        out.println("Добро пожаловать в OilBot Екатеринбурга!");
    }
}
