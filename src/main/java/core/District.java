package core;

public enum District {
    AKADEM("Академический"),
    ISETSKY("Верх-Исетский"),
    RAILWAY("Железнодорожный"),
    KIROV("Кировский"),
    LENIN("Ленинский"),
    OCTOBER("Октябрьский"),
    ORDZHONIKIDZE("Орджоникидзевский"),
    CHKALOV("Чкаловский");

    private final String name;
    District(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
