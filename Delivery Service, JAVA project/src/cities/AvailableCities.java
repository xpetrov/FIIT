package cities;


import java.io.Serializable;

public class AvailableCities implements Serializable{
    public static final AvailableCities ATHENS = new AvailableCities("Athens");
    public static final AvailableCities BERLIN = new AvailableCities("Berlin");
    public static final AvailableCities HELSINKI = new AvailableCities("Helsinki");
    public static final AvailableCities ISTANBUL = new AvailableCities("Istanbul");
    public static final AvailableCities KIEV = new AvailableCities("Kiev");
    public static final AvailableCities LISBON = new AvailableCities("Lisbon");
    public static final AvailableCities LONDON = new AvailableCities("London");
    public static final AvailableCities MADRID = new AvailableCities("Madrid");
    public static final AvailableCities MINSK = new AvailableCities("Minsk");
    public static final AvailableCities MOSCOW = new AvailableCities("Moscow");
    public static final AvailableCities OSLO = new AvailableCities("Oslo");
    public static final AvailableCities PARIS = new AvailableCities("Paris");
    public static final AvailableCities PRAGUE = new AvailableCities("Prague");
    public static final AvailableCities ROME = new AvailableCities("Rome");
    public static final AvailableCities STOCKHOLM = new AvailableCities("Stockholm");
    public static final AvailableCities VIENNA = new AvailableCities("Vienna");
    public static final AvailableCities WARSAW = new AvailableCities("Warsaw");


    public static AvailableCities[] value = {ATHENS, BERLIN, HELSINKI, ISTANBUL, KIEV, LISBON, LONDON,
            MADRID, MINSK, MOSCOW, OSLO, PARIS, PRAGUE, ROME, STOCKHOLM, VIENNA, WARSAW};

    private String name;

    private AvailableCities(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
