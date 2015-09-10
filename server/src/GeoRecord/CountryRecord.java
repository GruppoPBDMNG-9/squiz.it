package georecord;

import java.util.LinkedList;

public class CountryRecord extends GeoRecord{
    public CountryRecord(String name, int clicks, LinkedList<Object> cityList) {
        super(name, clicks, cityList);
    }
}
