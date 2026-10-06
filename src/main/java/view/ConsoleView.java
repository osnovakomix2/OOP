package view;

import core.District;
import core.FuelStation;
import core.FuelType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsoleView implements View {
    @Override
    public void showMessage(String data) {
        System.out.println(data);
    }

    @Override
    public void showStations(List<FuelStation> stations) {
        stations.stream()
                .forEach(x -> System.out.println(x));
    }

    @Override
    public void showDistricts(List<District> districts) {
        districts.stream()
                .forEach(x -> System.out.println(x));
    }

    @Override
    public void showFuelTypes(Set<FuelType> fuel) {
        String fuels = fuel.stream()
                .map(FuelType::toString)
                .collect(Collectors.joining(", "));
        System.out.println(fuels);
    }

    @Override
    public void showInfo() {
        String info = """
                Бот умеет находить заправки по району с топливом в наличии.
                Для этого выберете 1.
                Затем выберете нужный район. Бот выведет заправки, где люди сообщили о наличии топлива.
                
                Бот умеет фиксировать сообщения о конкретных АЗС.
                Для этого выберете 2. Затем выберете нужный район и заправку.
                Ответьте "+" если данный вид топлива в наличии, "-" если нет.
                
                -1 для выхода
                """;
        System.out.println(info);
    }

    @Override
    public void showGreeting() {
        String greeting = """
                Добро пожаловать в OilBot Екатеринбурга!
                Введите цифру нужного действия
                 0 Вызвать справку
                 1 Проверить наличие топлива в районе
                 2 Сообщить о ситуации на АЗС
                -1 Выйти
                """;
        System.out.println(greeting);
    }
}
