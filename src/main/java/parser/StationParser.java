package parser;

import core.FuelStation;

import java.io.InputStream;
import java.util.List;

public interface StationParser {
    public List<FuelStation> parseInputStream(InputStream in);
}
