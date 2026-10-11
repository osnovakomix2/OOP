package core;

public record Address(District district, String street, String building) {
    @Override
    public String toString() {
        return  "ул. " + street + ", д. " + building;
    }
}
