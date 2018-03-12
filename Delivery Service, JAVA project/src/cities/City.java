package cities;

import java.io.Serializable;
import java.util.*;

public abstract class City implements Serializable{

    private AvailableCities someCity;
    private int code;

    public City(AvailableCities someCity, int code) {
        this.someCity = someCity;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public AvailableCities getCity() {
        return someCity;
    }

    public static void createCities(List<City> cities) {
        cities.add(new Athens());
        cities.add(new Berlin());
        cities.add(new Helsinki());
        cities.add(new Istanbul());
        cities.add(new Kiev());
        cities.add(new Lisbon());
        cities.add(new London());
        cities.add(new Madrid());
        cities.add(new Minsk());
        cities.add(new Moscow());
        cities.add(new Oslo());
        cities.add(new Paris());
        cities.add(new Prague());
        cities.add(new Rome());
        cities.add(new Stockholm());
        cities.add(new Vienna());
        cities.add(new Warsaw());
    }
}





final class Athens extends City {
    public Athens() {
        super(AvailableCities.ATHENS,  1);
    }
}

final class Berlin extends City {
    public Berlin() {
        super(AvailableCities.BERLIN,  2);
    }
}

final class Helsinki extends City {
    public Helsinki() {
        super(AvailableCities.HELSINKI, 3);
    }
}

final class Istanbul extends City {
    public Istanbul() {
        super(AvailableCities.ISTANBUL,4);
    }
}

final class Kiev extends City {
    public Kiev() {
        super(AvailableCities.KIEV,  5);
    }
}

final class Lisbon extends City {
    public Lisbon() {
        super(AvailableCities.LISBON,  6);
    }
}

final class London extends City {
    public London() {
        super(AvailableCities.LONDON,  7);
    }
}

final class Madrid extends City {
    public Madrid() {
        super(AvailableCities.MADRID,  8);
    }
}

final class Minsk extends City {
    public Minsk() {
        super(AvailableCities.MINSK,  9);
    }
}

final class Moscow extends City{
    public Moscow() {
        super(AvailableCities.MOSCOW,  10);
    }
}

final class Oslo extends City {
    public Oslo() {
        super(AvailableCities.OSLO,  11);
    }
}

final class Paris extends City {
    public Paris() {
        super(AvailableCities.PARIS,  12);
    }
}

final class Prague extends City {
    public Prague() {
        super(AvailableCities.PRAGUE,  13);
    }
}

final class Rome extends City {
    public Rome() {
        super(AvailableCities.ROME,  14);
    }
}

final class Stockholm extends City {
    public Stockholm() {
        super(AvailableCities.STOCKHOLM,  15);
    }
}

final class Vienna extends City {
    public Vienna() {
        super(AvailableCities.VIENNA, 16);
    }
}

final class Warsaw extends City {
    public Warsaw() {
        super(AvailableCities.WARSAW,  17);
    }
}