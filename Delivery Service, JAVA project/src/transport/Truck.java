/**
 * Truck class
 */
package transport;

import java.io.Serializable;

public class Truck extends Transport implements Serializable{
    /**
     * Class constructor
     * @param codeOfHometown specifying the code of Hometown
     * @param codeOfDestinationTown specifying code of DestinationTown
     * @param name specifying name of created transport. Includes names of working area cities, e.g. "Athens/Istanbul"
     */
    public Truck(int codeOfHometown, int codeOfDestinationTown, String name) {
        super(TypeOfTransport.TRUCK, 700, 4000, codeOfHometown, codeOfDestinationTown, name); // super([type], [kg], [m^3], [km/h]);
    }
}
