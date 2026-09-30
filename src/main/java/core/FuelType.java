package core;

public enum FuelType {
    DT("ДТ"),
    AI_100("АИ-100"),
    AI_95("АИ-95"),
    AI_92("АИ-92");
    private final String name;
    FuelType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
